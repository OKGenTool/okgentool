package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Response
import datamodel.Response.*
import datamodel.SchemaProps
import generator.capitalize
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

        when (response) {
            is ResponseRef -> {
                val simpleName = SchemaProps.getRefSimpleName(response.schemaRef)
                varType = LambdaTypeName.get(
                    parameters = arrayOf(ClassName(Packages.MODEL, simpleName.capitalize())),
                    returnType = UNIT
                ).suspending()
            }

            is ResponseRefColl -> {
                val simpleName = SchemaProps.getRefSimpleName(response.schemaRef)
                val param = LIST.parameterizedBy(
                    ClassName(Packages.MODEL, simpleName.capitalize())
                )
                varType = LambdaTypeName.get(
                    parameters = arrayOf(param),
                    returnType = UNIT
                ).suspending()
            }

            is ResponseNoContent -> {
                varType = LambdaTypeName.get(returnType = UNIT).suspending()
            }

            is ResponseInline -> {
                varType = LambdaTypeName.get(
                    parameters = arrayOf(response.type.kotlinType),
                    returnType = UNIT
                ).suspending()
            }
        }
        ResponseProp(
            response,
            PropertySpec
                .builder(varName, varType)
                .initializer(varName)
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

    responseProps.map {
        val propParams = (it.responseType.type as LambdaTypeName).parameters
        val respFun = FunSpec.builder(it.responseType.name)
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

        respFun.addCode(
            """
            call.respond(
                HttpStatusCode(${it.response.statusCodeInt}, "${it.response.description}"),
                ${paramName ?: ""}
            )
            """.trimIndent()
        )

        functions.add(respFun.build())
    }
    return functions
}

