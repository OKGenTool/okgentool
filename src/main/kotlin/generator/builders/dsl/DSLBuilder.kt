package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.builders.routing.routes.buildReadRequestResult
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

fun buildDSLOperations(dslOperations: List<DSLOperation>, componentNames: List<String>, basePath: String) {
    for (operation in dslOperations) {
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())

        val parameters: MutableList<GenParameter> = mutableListOf()
        var requestGenParameter: GenParameter? = null

        var requestType: TypeSpec? = null
        var responseType: TypeSpec? = null

        //Build Request class
        requestType = buildRequestClass(operation, parameters)
        requestType?.let { fileSpec.addType(requestType) }

        //Build Response Class
        val responseProps = getResponseProps(operation.name, operation.responses!!)
        responseType = getResponseType(operation.name, responseProps.map { it.responseType })
        fileSpec.addType(responseType)

        //Build Operation main class
        parameters.add(
            GenParameter(
                "call",
                ApplicationCall::class.asTypeName(),
                Visibility.PRIVATE
            )
        )
        fileSpec.addType(
            getOperationType(
                parameters,
                operation.name,
                requestType,
                responseType,
                responseProps
            )
        )
            .addImport("io.ktor.server.response", "respond")
            .addImport("io.ktor.http", "HttpStatusCode")
            .addImport(Packages.ROUTES, "ReadRequestResult", "RequestErrorInvalidArgument")

        writeFile(fileSpec.build(), basePath)
    }

    buildApiOperations(basePath)
    buildReadRequestResult(basePath)
    buildOkGenDsl(dslOperations, componentNames, basePath)
}


private fun getOperationType(
    genParameters: List<GenParameter?>,
    operationName: String,
    request: TypeSpec?,
    response: TypeSpec?,
    responseProps: List<ResponseProp>,
): TypeSpec {
    val mainClass = TypeSpec.classBuilder(operationName.capitalize())

    if (!genParameters.isEmpty()) {
        mainClass.getConstructor(genParameters)
    }

//    val reqVarName = genParameters.first()?.name
    var reqVarName = ""
    genParameters.forEach {
        if (it?.name != "call")
            reqVarName += "${it?.name}, "
    }

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
    var responseCode = "${response?.name}("
    response?.propertySpecs?.map {
        val propParams = (it.type as LambdaTypeName).parameters
        if (!propParams.isEmpty()) {
            var propParamsCode = ""
            propParams.map {
                propParamsCode += "${getVarNameFromParam(it.toString())},"
            }
            responseCode += "\n${it.name} = { $propParamsCode -> ${it.name}($propParamsCode)},\n"
        } else {
            responseCode += "\n${it.name} = { ${it.name}() },"
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
    getResponseFunctions(responseProps)
        .map {
            mainClass.addFunction(it)
        }

    return mainClass.build()
}


fun getBodyAsParameter(body: BodyNew): GenParameter {
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


