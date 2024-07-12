package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.*
import generator.model.Imports.*
import generator.model.Imports.Companion.addCustomImport
import generator.model.Packages
import generator.model.Parameter
import generator.model.ResponseProp
import generator.model.Visibility
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.server.application.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("DslOperationsBuilder.kt")

fun buildDSLOperations(
    dslOperations: List<DSLOperation>,
    paramsToImportInOkGenDSL: MutableList<Parameter>,
    destinationPath: String
) {

    for (operation in dslOperations) {
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())

        val parameters: MutableList<Parameter> = getOperationParameters(operation)
        paramsToImportInOkGenDSL.addAll(parameters.filter {
            !it.enum.isNullOrEmpty()
        })

        //Build Params Classes
        val paramsTypes: List<TypeSpec> = buildOperationsParams(operation.name, parameters)

        //Build Request class
        val requestType = buildRequestClass(operation, parameters)
        requestType?.let { fileSpec.addType(requestType) }

        //Build Response Class
        val responseProps = getResponseProps(operation.name, operation.responses!!)
        val responseType = getResponseType(operation.name, responseProps.map { it.responseType })
        fileSpec.addType(responseType)

        //Build Operation main class
        parameters.add(
            Parameter(
                "call", ApplicationCall::class.asTypeName(), Visibility.PRIVATE
            )
        )
        fileSpec.addType(
            getOperationType(
                parameters, operation.name, requestType, responseType, responseProps
            )
        )

        //Add Param Classes to Operation
        paramsTypes.map {
            fileSpec.addType(it)
        }

        fileSpec
            .addCustomImport(KTOR_SERVER_RESPOND)
            .addCustomImport(KTOR_HTTP_STATUS_CODE)
            .addCustomImport(KTOR_SERVER_RESPONSE_HEADER)

        writeFile(fileSpec.build(), destinationPath)
    }
}


private fun getOperationType(
    parameters: List<Parameter?>,
    operationName: String,
    request: TypeSpec?,
    response: TypeSpec?,
    responseProps: List<ResponseProp>,
): TypeSpec {
    val mainClass = TypeSpec.classBuilder(operationName.capitalize())

    if (parameters.isNotEmpty()) {
        mainClass.buildConstructor(parameters)
    }

    var reqVarName = ""
    parameters.forEach {
        if (it?.name != "call") reqVarName += "${it?.name}, "
    }

    //Add request var
    if (request != null) {
        mainClass.addProperty(
            PropertySpec.builder(
                "request", ClassName(Packages.DSLOPERATIONS, request.name!!)
            ).initializer("${request.name}($reqVarName)").build()
        )
    }

    //Add response var
    var responseCode = "${response?.name}("
    response?.propertySpecs?.map {
        val propParams = (it.type as LambdaTypeName).parameters
        if (propParams.isNotEmpty()) {
            var propParamsCode = ""
            val firstParam = propParams.first()
            propParamsCode += "${getVarNameFromParam(firstParam.toString())}, "

            responseProps.map { responseProp ->
                responseProp.response.headers?.let {
                    it.map { header ->
                        val headerName = header.name.replace("-", "").decapitalize()
                        propParamsCode += "$headerName, "
                    }
                }
            }
            responseCode += "\n${it.name} = { $propParamsCode -> ${it.name}($propParamsCode)},\n"
        } else {
            responseCode += "\n${it.name} = { ${it.name}() },"
        }
    }
    responseCode += "\n)"

    //Add unsafe var
    mainClass.addProperty(
        PropertySpec.builder(
            "unsafe", ClassName(Packages.DSLOPERATIONS, "Unsafe")
        ).initializer("Unsafe(call)").build()
    )

    mainClass.addProperty(
        PropertySpec.builder(
            "response",
            ClassName(Packages.DSLOPERATIONS, response?.name!!),
        ).initializer(responseCode).build()
    )

    //Add Response Functions
    getResponseFunctions(responseProps).map {
        mainClass.addFunction(it)
    }

    return mainClass.build()
}

private fun getOperationParameters(operation: DSLOperation): MutableList<Parameter> {
    val parameters: MutableList<Parameter> = mutableListOf()

    //When using body
    operation.requestBody?.let {
        parameters.add(getBodyParameter(it))
    }

    //Parameters when using query string
    operation.parameters.takeIf { !it.isNullOrEmpty() }?.let {
        parameters.addAll(getParametersFromQueryOrPath(operation))
    }

    return parameters
}

private fun getBodyParameter(body: Body): Parameter {
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

    return Parameter(name, type?.nullable()!!)
}

/**
 * Get parameters when using requests with query strings or path parameter
 */
private fun getParametersFromQueryOrPath(operation: DSLOperation): List<Parameter> {
    val params: MutableList<Parameter> = mutableListOf()

    operation.parameters?.forEach { parameter ->
        var typeName: TypeName? = null
        var enum: List<String>? = null

        when (parameter) {
            is QueryParameterEnum -> {
                val className = "${parameter.name.capitalize()}Param"
                typeName = LIST.parameterizedBy(ClassName(Packages.DSLOPERATIONS, className))
                enum = parameter.enum.map { it.toString() }
            }

            is QueryParameterArray -> {
                typeName = LIST.parameterizedBy(parameter.itemsType.kotlinType)
            }

            is QueryParameterSingle, is HeaderParameter -> typeName = STRING

            is PathParameter -> typeName = parameter.type.kotlinType

            else -> {
                logger.warn("${operation.name}: Parameter not implemented: $parameter")

            }
        }

        params.add(
            Parameter(
                parameter.name,
                typeName?.nullable()!!,
                Visibility.PUBLIC,
                enum,
            )
        )
    }
    return params
}



