package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.Response
import datamodel.SchemaProps
import generator.capitalize
import generator.getVarNameFromParam
import generator.model.Packages
import generator.model.ResponseProp

fun getResponseProps(operationName: String, responses: List<Response>): List<ResponseProp> {
    return responses.map {
        val varName = "${operationName}Response${it.statusCodeStr.capitalize()}"

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

