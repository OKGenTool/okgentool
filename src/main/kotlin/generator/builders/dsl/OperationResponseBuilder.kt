package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.DSLHeader
import datamodel.Response
import datamodel.Response.*
import datamodel.SchemaProps
import generator.capitalize
import generator.decapitalize
import generator.getVarNameFromParam
import generator.model.Packages
import generator.model.ResponseProp
import generator.suspending
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("OperationResponseBuilder.kt")

fun getResponseProps(operationName: String, responses: List<Response>): List<ResponseProp> {
    return responses.map { response ->
        val varName = "${operationName}Response${response.statusCodeStr.capitalize()}"

        var varType: TypeName? = null

        val headersParams = getHeadersParams(response.headers)
        val propKdoc: CodeBlock.Builder = CodeBlock.builder()

        when (response) {
            is ResponseRef -> {
                val simpleName = SchemaProps.getRefSimpleName(response.schemaRef)
                varType = LambdaTypeName.get(
                    parameters = arrayOf(ClassName(Packages.MODEL, simpleName.capitalize())) + headersParams,
                    returnType = UNIT
                ).suspending()
                propKdoc.add("@param $simpleName\n")
            }

            is ResponseRefColl -> {
                val simpleName = SchemaProps.getRefSimpleName(response.schemaRef)
                val param: TypeName = LIST.parameterizedBy(
                    ClassName(Packages.MODEL, simpleName.capitalize())
                )

                varType = LambdaTypeName.get(
                    parameters = arrayOf(param) + headersParams,
                    returnType = UNIT
                ).suspending()
                propKdoc.add("@param $simpleName\n")
            }

            is ResponseNoContent -> {
                varType = LambdaTypeName.get(
                    parameters = headersParams,
                    returnType = UNIT
                ).suspending()
            }

            is ResponseInline -> {
                varType = LambdaTypeName.get(
                    parameters = arrayOf(response.type.kotlinType) + headersParams,
                    returnType = UNIT
                ).suspending()
                propKdoc.add("@param ${response.type.name}\n")
            }
        }
        propKdoc.addHeaders(response.headers)

        ResponseProp(
            response,
            PropertySpec
                .builder(varName, varType)
                .initializer(varName)
                .addKdoc(propKdoc.build())
                .build()
        )
    }
}

fun getResponseType(operationName: String, propertySpecs: List<PropertySpec>): TypeSpec {
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

fun getResponseFunctions(responseProps: List<ResponseProp>): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()

    responseProps.map { responseProp ->
        val propParams = (responseProp.responseType.type as LambdaTypeName).parameters
        val respFun = FunSpec.builder(responseProp.responseType.name)
            .addModifiers(KModifier.PRIVATE, KModifier.SUSPEND)
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

        responseProp.response.headers?.let {
            it.map { header ->
                val nameVal = header.name.replace("-", "").decapitalize()
                respFun.addParameter(
                    nameVal,
                    header.dataType.kotlinType
                )
                respFun.addCode("call.response.header(\"${header.name}\", $nameVal)\n")
            }
        }

        respFun.addCode(
            """
            call.respond(
                HttpStatusCode(${responseProp.response.statusCodeInt}, "${responseProp.response.description}"),
                ${paramName ?: ""}
            )
            """.trimIndent()
        )

        functions.add(respFun.build())
    }
    return functions
}

fun getHeadersParams(headers: List<DSLHeader>?): Array<ClassName> {
    if (headers == null) return arrayOf()
    val headersList: MutableList<ClassName> = mutableListOf()

    headers.map {
        headersList.add(it.dataType.kotlinType)
    }

    return headersList.toTypedArray()
}

fun CodeBlock.Builder.addHeaders(headers: List<DSLHeader>?): CodeBlock.Builder {
    if (headers == null) return this

    headers.map {
        this.add("@param ${it.name.replace("-", "")} ${it.description}\n")
    }
    return this
}

