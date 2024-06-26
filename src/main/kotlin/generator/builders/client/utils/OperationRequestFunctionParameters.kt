package generator.builders.client.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import datamodel.*
import generator.capitalize
import generator.decapitalize
import generator.model.Packages

fun getFunctionParameters(operation: DSLOperation, schemaNames: List<String>): List<ParameterSpec> {
    val parameters = mutableListOf<ParameterSpec>()

    if (!operation.parameters.isNullOrEmpty()) {
        for(parameter in operation.parameters) {
            when(parameter.`in`){
                In.QUERY -> parameters.add(getQueryParameterSpec(parameter))
                In.PATH -> parameters.add(getPathParameterSpec(parameter))
                In.HEADER -> {}// TODO: parameters.add(getHeaderParameterSpec(parameter))
                In.COOKIE -> {}
            }
        }
    }

    if (operation.requestBody != null) {
        parameters.add(getBodyParameterSpec(operation.requestBody, schemaNames))
    }

    return parameters
}

private fun getQueryParameterSpec(parameter: DSLParameter): ParameterSpec {
    if (parameter is QueryParameterSingle){
        return ParameterSpec
            .builder(parameter.name, String::class.asTypeName().copy(nullable = true))
            .build()
    }

    if (parameter is QueryParameterArray){
        return ParameterSpec
            .builder(parameter.name, List::class.asTypeName().parameterizedBy(parameter.itemsType.kotlinType).copy(nullable = true))
            .build()
    }

    // TODO: Add support for QueryParameterEnum
    return ParameterSpec
        .builder(parameter.name, String::class.asTypeName().copy(nullable = true))
        .build()

}

private fun getPathParameterSpec(parameter: DSLParameter): ParameterSpec {
    return if (parameter is PathParameter){
        ParameterSpec.builder(parameter.name, parameter.type.kotlinType)
            .build()
    } else {
        throw IllegalArgumentException("Parameter is not a PathParameter")
    }
}

private fun getHeaderParameterSpec(parameter: DSLParameter): ParameterSpec {
    TODO("Not yet implemented")
}

private fun getBodyParameterSpec(requestBody: Body, schemaNames: List<String>): ParameterSpec {
    if (requestBody is BodyRef) {
        val schemaName = requestBody.schemaRef.substringAfterLast("/").capitalize()
        if (schemaNames.contains(schemaName)) {
            return ParameterSpec
                .builder(schemaName.decapitalize(), ClassName(Packages.MODEL, schemaName.capitalize()))
                .build()
        }
        throw IllegalArgumentException("Schema $schemaName not found")
    }

    if (requestBody is BodyCollRef) {
        val name = "${requestBody.className}List"
        return ParameterSpec
            .builder(name.decapitalize(), List::class.asTypeName().parameterizedBy(
                ClassName(
                    Packages.MODEL, requestBody.className.capitalize()
                )))
            .build()
    }

    if (requestBody is BodyObj) {
        return ParameterSpec
            .builder("body", requestBody.dataType.kotlinType)
            .build()
    }

    if (requestBody is BodyCollPojo) {
        return ParameterSpec
            .builder("body", List::class.asTypeName().parameterizedBy(requestBody.dataType.kotlinType))
            .build()
    }

    throw IllegalArgumentException("Body type not supported")
}
