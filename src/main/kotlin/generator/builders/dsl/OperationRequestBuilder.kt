package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.DSLOperation
import datamodel.In
import generator.builders.getConstructor
import generator.capitalize
import generator.model.Parameter
import generator.model.Packages
import generator.model.Visibility
import generator.nullable


fun buildRequestClass(operation: DSLOperation, parameters: List<Parameter>)
        : TypeSpec? {
    //Build request class when using body
    operation.requestBody?.let {
        return getRequestType(parameters, operation.name)
    }

    //Build request class when using query string or path parameters
    operation.parameters.takeIf { !it.isNullOrEmpty() }?.let {
        return getRequestWithParam(operation, parameters)
    }
    return null
}

/**
 * Build request class when using query parameters
 */
fun getRequestWithParam(operation: DSLOperation, params: List<Parameter>): TypeSpec {
    val requestClass = TypeSpec.classBuilder("${operation.name.capitalize()}Request")
        .addModifiers(KModifier.DATA)
        .getConstructor(params)
    return requestClass.build()
}


fun getRequestType(parameters: List<Parameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .getConstructor(parameters)
        .build()
}

