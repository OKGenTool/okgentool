package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.capitalize
import generator.getVarNameFromParam
import generator.model.GenParameter
import generator.model.Packages
import generator.model.Visibility
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.server.application.*
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("DSLBuilder.kt")

//TODO receive dslOperations instead of the entire dataModel
fun buildDSLOperations(dataModel: DataModel, basePath: String) {
    val dslOperations = dataModel.dslOperations
    for (operation in dslOperations) {
        if (operation.name.equals("createUsersWithListInput")) logger.info("createUsersWithListInput")
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())

        var requestGenParameter: GenParameter? = null

        var request: TypeSpec? = null
        var response: TypeSpec? = null

        //Build Request class
        if (operation.requestBody != null) {
            requestGenParameter = getParameter(operation.requestBody)
            request = getRequest(listOf(requestGenParameter), operation.name)
            fileSpec.addType(request)
        }

        //Build Response Class
        if (operation.responses != null) {
            response = getResponse(operation)
            fileSpec.addType(response)
        }

        //Build Operation main class
        fileSpec.addType(
            getMainClass(
                listOf(
                    requestGenParameter,
                    GenParameter(
                        "call",
                        ApplicationCall::class.asTypeName(),
                        Visibility.PRIVATE
                    )
                ),
                operation.name,
                request,
                response
            )
        )
            .addImport("io.ktor.server.response", "respond")
            .addImport("io.ktor.http", "HttpStatusCode")

        writeFile(fileSpec.build(), basePath)
    }
}

private fun getMainClass(
    genParameters: List<GenParameter?>,
    operationName: String,
    request: TypeSpec?,
    response: TypeSpec?,
): TypeSpec {
    val mainClass = TypeSpec.classBuilder(operationName.capitalize())

    if (!genParameters.isEmpty()) {
        mainClass.getConstructor(genParameters)
    }

    val reqVarName = genParameters.first()?.name

    //Add request var
    if (request != null) {
        mainClass.addProperty(
            PropertySpec
                .builder(
                    "request",
                    ClassName(Packages.DSLOPERATIONS, request.name!!),
                    KModifier.PRIVATE
                )
                .initializer("${request.name}($reqVarName)")
                .build()
        )
    }

    //Add response var
    var responseCode = "${response?.name}(\n"
    response?.propertySpecs?.map {
        val propParams = (it.type as LambdaTypeName).parameters
        if (!propParams.isEmpty()) {
            var propParamsCode = ""
            propParams.map {
                propParamsCode += "${getVarNameFromParam(it.toString())},"
            }
            responseCode += "${it.name} = { $propParamsCode -> ${it.name}($propParamsCode)},\n"
        } else {
            responseCode += "${it.name} = { ${it.name}() }"
        }
    }
    responseCode += "\n)"

    mainClass.addProperty(
        PropertySpec
            .builder(
                "response",
                ClassName(Packages.DSLOPERATIONS, response?.name!!),
                KModifier.PRIVATE
            )
            .initializer(responseCode)
            .build()
    )

    //TODO Add Response Functions
    getResponseFunctions(response.propertySpecs)
        .map {
            mainClass.addFunction(it)
        }

    return mainClass.build()
}

private fun getResponseFunctions(properties: List<PropertySpec>): List<FunSpec> {
    val responses = mutableListOf<FunSpec>()

    properties.map {
        val propParams = (it.type as LambdaTypeName).parameters
        val respFun = FunSpec.builder(it.name)
            .addModifiers(KModifier.PRIVATE, KModifier.SUSPEND)
        propParams.map {
            respFun.addParameter(
                getVarNameFromParam(it.type.toString()), it.type
            )
        }

        //TODO get the appropriate code for this response
        respFun.addCode(
            """
            call.respond(
                HttpStatusCode.OK.description("Successful operation"),
                pet
            )
            """.trimIndent()
        )

        responses.add(respFun.build())
    }



    return responses
}


private fun getResponse(operation: DSLOperation): TypeSpec {
    val propertySpecs = getProperties(operation.name, operation.responses!!)

    val responseBuilder = TypeSpec.classBuilder("${operation.name.capitalize()}Response").addModifiers(KModifier.DATA)

    //Build constructor
    val const = FunSpec.constructorBuilder()
    propertySpecs?.map {
        responseBuilder.addProperty(it)
        const.addParameter(it.name, it.type)
    }
    responseBuilder.primaryConstructor(const.build())

    return responseBuilder.build()
}

private fun getProperties(operationName: String, responses: List<ResponseNew>): List<PropertySpec> {
    return responses.map { response ->
        val varName = "${operationName}Response${response.statusCode.capitalize()}"

        val varType: TypeName
        val schemaRef = response.schemaRef
        if (schemaRef.isNullOrEmpty()) {
            varType = LambdaTypeName.get(returnType = UNIT).copy(suspending = true)
        } else {
            val simpleName = SchemaProps.getRefSimpleName(schemaRef)
            varType = LambdaTypeName.get(
                parameters = arrayOf(ClassName(Packages.MODEL, simpleName.capitalize())),
                returnType = UNIT
            ).copy(suspending = true)

        }
        PropertySpec
            .builder(varName, varType)
            .initializer(varName)
            .build()

    }
}

private fun getRequest(genParameters: List<GenParameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .getConstructor(genParameters)
        .build()
}

private fun TypeSpec.Builder.getConstructor(genParameters: List<GenParameter?>): TypeSpec.Builder {
    val constructor = FunSpec.constructorBuilder()
    val properties: MutableList<PropertySpec> = mutableListOf()

    genParameters.map {
        if (it != null) {
            constructor.addParameter(it.name, it.type)
            properties.add(
                PropertySpec.builder(it.name, it.type, it.visibility.modifier)
                    .initializer(it.name)
                    .build()
            )
        }
    }

    return this.primaryConstructor(
        constructor.build()
    ).addProperties(properties)
}

private fun getParameter(body: BodyNew): GenParameter {
    var name: String = ""
    var type: TypeName? = null

    when (body) {
        is BodyRef -> {
            name = body.schemaRef
            type = ClassName(
                Packages.MODEL, body.schemaRef.capitalize()
            )
        }

        is BodyObj -> {
            name = if (body.contentTypes[0] == OctetStream.toString()) "binFile"
            else "prop"

            type = body.dataType.kotlinType
        }

        //When is a collection of regular POJOs
        is BodyCollPojo -> {
            //Use the first tag (if any) to build the var name
            name = body.tags?.firstOrNull()?.let { "${it}List" } ?: "list"
            type = List::class.asTypeName().parameterizedBy(body.dataType.kotlinType)
        }

        is BodyCollRef -> {
            name = "${body.className}List"
            type = List::class.asTypeName().parameterizedBy(
                ClassName(
                    Packages.MODEL, body.className.capitalize()
                )
            )
        }
    }

    return GenParameter(name, type!!)
}


