package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import generator.builders.buildConstructor
import generator.capitalize
import generator.model.Parameter


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
        .buildConstructor(params)
    return requestClass.build()
}


fun getRequestType(parameters: List<Parameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .buildConstructor(parameters)
        .build()
}

