package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.capitalize
import generator.getVarNameFromParam
import generator.model.GenParameter
import generator.model.Packages
import generator.model.ResponseProp
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

        var requestType: TypeSpec? = null
        var responseType: TypeSpec? = null

        //Build Request class
        if (operation.requestBody != null) {
            requestGenParameter = getParameter(operation.requestBody)
            requestType = getRequestType(listOf(requestGenParameter), operation.name)
            fileSpec.addType(requestType)
        }

        //Build Response Class
        val responseProps = getResponseProps(operation.name, operation.responses!!)
        responseType = getResponseType(operation.name, responseProps.map { it.responseType })
        fileSpec.addType(responseType)

        //Build Operation main class
        fileSpec.addType(
            getOperationType(
                listOf(
                    requestGenParameter,
                    GenParameter(
                        "call",
                        ApplicationCall::class.asTypeName(),
                        Visibility.PRIVATE
                    )
                ),
                operation.name,
                requestType,
                responseType,
                operation.responses,
                responseProps
            )
        )
            .addImport("io.ktor.server.response", "respond")
            .addImport("io.ktor.http", "HttpStatusCode")

        writeFile(fileSpec.build(), basePath)
    }
}

private fun getOperationType(
    genParameters: List<GenParameter?>,
    operationName: String,
    request: TypeSpec?,
    response: TypeSpec?,
    responses: List<ResponseNew>,
    responseProps: List<ResponseProp>,
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
                    ClassName(Packages.DSLOPERATIONS, request.name!!)
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
            )
            .initializer(responseCode)
            .build()
    )

    //TODO Add Response Functions
    getResponseFunctions(response.propertySpecs, responses, responseProps)
        .map {
            mainClass.addFunction(it)
        }

    return mainClass.build()
}

private fun getResponseFunctions(
    properties: List<PropertySpec>,
    responses: List<ResponseNew>,
    responseProps: List<ResponseProp>,
): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()

    responseProps.map {
        val propParams = (it.responseType.type as LambdaTypeName).parameters
        val respFun = FunSpec.builder(it.responseType.name)
            .addModifiers(KModifier.PRIVATE, KModifier.SUSPEND)
//        propParams.map {
//            respFun.addParameter(
//                getVarNameFromParam(it.type.toString()), it.type
//            )
//        }
        var param: ParameterSpec? = null
        var paramName: String? = null
        if (propParams.isNotEmpty()) {
            param = propParams.first()
            paramName = getVarNameFromParam(param.type.toString())
            respFun.addParameter(
                paramName,
                param.type
            )
        }


        //TODO get the appropriate code for this response
        val statusCode = it.response.statusCode.toIntOrNull()
        if (statusCode == null) //TODO What to do when the status code is "default"?
            logger.error("Error in status code: ${it.response.statusCode}")

        respFun.addCode(
            """
            call.respond(
                HttpStatusCode(${statusCode}, "${it.response.description}"),
                ${paramName ?: ""}
            )
            """.trimIndent()
        )

        functions.add(respFun.build())
    }



    return functions
}


private fun getResponseType(operationName: String, propertySpecs: List<PropertySpec>): TypeSpec {
//    val propertySpecs = getProperties(operation.name, operation.responses!!)

    val responseBuilder = TypeSpec
        .classBuilder("${operationName.capitalize()}Response")
        .addModifiers(KModifier.DATA)

    //Build constructor
    val const = FunSpec.constructorBuilder()
    propertySpecs.map {
        responseBuilder.addProperty(it)
        const.addParameter(it.name, it.type)
    }
    responseBuilder.primaryConstructor(const.build())

    return responseBuilder.build()
}

//private fun getProperties(operationName: String, responses: List<ResponseNew>): List<PropertySpec> {
private fun getResponseProps(operationName: String, responses: List<ResponseNew>): List<ResponseProp> {
    return responses.map {
        val varName = "${operationName}Response${it.statusCode.capitalize()}"

        val varType: TypeName
        val schemaRef = it.schemaRef
        if (schemaRef.isNullOrEmpty()) {
            varType = LambdaTypeName.get(returnType = UNIT).copy(suspending = true)
        } else {
            val simpleName = SchemaProps.getRefSimpleName(schemaRef)
            varType = LambdaTypeName.get(
                parameters = arrayOf(ClassName(Packages.MODEL, simpleName.capitalize())),
                returnType = UNIT
            ).copy(suspending = true)

        }
        ResponseProp(
            it,
            PropertySpec
                .builder(varName, varType)
                .initializer(varName)
                .build()
        )
    }
}

private fun getRequestType(genParameters: List<GenParameter>, operationName: String): TypeSpec {
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


