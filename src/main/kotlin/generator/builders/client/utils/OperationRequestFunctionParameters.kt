package generator.builders.client.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import datamodel.*
import generator.capitalize
import generator.decapitalize
import generator.model.ClientFunctionParameter
import generator.model.ContentType
import generator.model.Packages

fun getFunctionParameters(operation: DSLOperation, schemaNames: List<String>): Pair<List<ParameterSpec>, List<ClientFunctionParameter>> {
    val parametersSpec = mutableListOf<ParameterSpec>()
    val parametersObject = mutableListOf<ClientFunctionParameter>()

    if (!operation.parameters.isNullOrEmpty()) {
        for(parameter in operation.parameters) {
            when(parameter.`in`){
                In.QUERY -> {
                    val (parameterSpec, parameterObject) = getQueryParameterSpec(parameter)
                    parametersSpec.add(parameterSpec)
                    parametersObject.add(parameterObject)
                }
                In.PATH -> {
                    val (parameterSpec, parameterObject) = getPathParameterSpec(parameter)
                    parametersSpec.add(parameterSpec)
                    parametersObject.add(parameterObject)
                }
                In.HEADER -> {}// TODO: parameters.add(getHeaderParameterSpec(parameter))
                In.COOKIE -> {}
            }
        }
    }

    if (operation.requestBody != null) {
        val (parameterSpec, parameterObject) = getBodyParameterSpec(operation.requestBody, schemaNames)
        parametersSpec.add(parameterSpec)
        parametersObject.add(parameterObject)
    }

    return Pair(parametersSpec, parametersObject)
}

private fun getQueryParameterSpec(parameter: DSLParameter): Pair<ParameterSpec, ClientFunctionParameter> {
    if (parameter is QueryParameterSingle){
        val type = String::class.asTypeName().copy(nullable = true)
        return Pair(
            ParameterSpec
                .builder(parameter.name, type)
                .build(),
            ClientFunctionParameter(
                name = parameter.name,
                dataType = type,
                isQuery = true,
            )
        )
    }

    if (parameter is QueryParameterArray){
        val type = List::class.asTypeName().parameterizedBy(parameter.itemsType.kotlinType).copy(nullable = true)
        return Pair(
            ParameterSpec
                .builder(parameter.name, type)
                .build(),
            ClientFunctionParameter(
                name = parameter.name,
                dataType = type,
                isQuery = true,
                isList = true,
            )
        )
    }

    // TODO: Add support for QueryParameterEnum
    val type = String::class.asTypeName().copy(nullable = true)
    return Pair(
        ParameterSpec
            .builder(parameter.name, type)
            .build(),
        ClientFunctionParameter(
            name = parameter.name,
            dataType = type,
            isQuery = true,
        )
    )
}

private fun getPathParameterSpec(parameter: DSLParameter): Pair<ParameterSpec, ClientFunctionParameter> {
    return if (parameter is PathParameter){
        Pair(
            ParameterSpec
                .builder(parameter.name, parameter.type.kotlinType)
                .build(),
            ClientFunctionParameter(
                name = parameter.name,
                dataType = parameter.type.kotlinType,
                isPath = true,
            )
        )
    } else {
        throw IllegalArgumentException("Parameter is not a PathParameter")
    }
}

private fun getHeaderParameterSpec(parameter: DSLParameter): ParameterSpec {
    TODO("Not yet implemented")
}

private fun getBodyParameterSpec(requestBody: Body, schemaNames: List<String>): Pair<ParameterSpec, ClientFunctionParameter> {
    val contentType = getBodyContentType(requestBody)

        if (requestBody is BodyRef) {
        val schemaName = requestBody.schemaRef.substringAfterLast("/").capitalize()
        if (schemaNames.contains(schemaName)) {
            val type = ClassName(Packages.MODEL, schemaName.capitalize())

            return Pair(
                ParameterSpec
                    .builder(schemaName.decapitalize(), type)
                    .build(),
                ClientFunctionParameter(
                    name = schemaName.decapitalize(),
                    dataType = type,
                    isBody = true,
                    bodyContentType = contentType,
                )
            )
        }
        throw IllegalArgumentException("Schema $schemaName not found")
    }

    if (requestBody is BodyCollRef) {
        val name = "${requestBody.className}List"
        val type = List::class.asTypeName().parameterizedBy(
            ClassName(
                Packages.MODEL, requestBody.className.capitalize()
            ))
        return Pair(
            ParameterSpec
                .builder(name.decapitalize(), type)
                .build(),
            ClientFunctionParameter(
                name = name.decapitalize(),
                dataType = type,
                isBody = true,
                isList = true,
                bodyContentType = contentType,
            )
        )
    }

    if (requestBody is BodyObj) {
        val type = requestBody.dataType.kotlinType
        return Pair(
            ParameterSpec
                .builder("body", type)
                .build(),
            ClientFunctionParameter(
                name = "body",
                dataType = type,
                isBody = true,
                bodyContentType = contentType,
            )
        )
    }

    if (requestBody is BodyCollPojo) {
        val type = List::class.asTypeName().parameterizedBy(requestBody.dataType.kotlinType)
        return Pair(
            ParameterSpec
                .builder("body", type)
                .build(),
            ClientFunctionParameter(
                name = "body",
                dataType = type,
                isBody = true,
                isList = true,
                bodyContentType = contentType,
            )
        )
    }

    throw IllegalArgumentException("Body type not supported")
}

fun getBodyContentType(requestBody: Body): ContentType {
    if (requestBody.contentTypes.contains(ContentType.XML.description)) {
        return ContentType.XML
    }
    return ContentType.JSON
}
