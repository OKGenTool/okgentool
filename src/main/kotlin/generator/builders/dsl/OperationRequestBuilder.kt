package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.DSLOperation
import datamodel.In
import generator.capitalize
import generator.model.Parameter
import generator.model.Packages
import generator.model.Visibility
import generator.nullable


fun buildRequestClass(operation: DSLOperation, parameters: MutableList<Parameter>)
        : TypeSpec? {
    //Build request class when using body
    operation.requestBody?.let {
        parameters.add(getBodyAsParameter(it))
        return getRequestType(parameters, operation.name)
    }

    //Build request class when using query string or path parameters
    operation.parameters.takeIf { !it.isNullOrEmpty() }?.let {
        parameters.addAll(getParameters(operation))
        return getRequestWithParam(operation, parameters)
    }
    return null
}

/**
 * Build request class when using query parameters
 */
fun getRequestWithParam(operation: DSLOperation, params: List<Parameter>): TypeSpec {
    val validation = getValidationCompObj(params)

    //TODO must implement multiple validations. One for each query string
    val firstParameter = operation.parameters?.first()
    val paramName = firstParameter?.name.toString()
    val paramValidation = "valid${paramName.capitalize()}"

    // Create the init block
    val initBlock = CodeBlock.builder()
    if (validation != null) {
        initBlock.beginControlFlow("if ($paramName in $paramValidation)")
            .addStatement("result = ReadRequestResult.Success($paramName!!)")
            .nextControlFlow("else")
            .addStatement(
                "result = ReadRequestResult.Failure(\nRequestErrorInvalidArgument(\n\t\"\$$paramName is not a valid value. Valid values are: \$$paramValidation\"\n\t)\n)"
            )
            .endControlFlow()
    }

    val resultProp = PropertySpec.builder(
        "result", ClassName(Packages.ROUTES, "ReadRequestResult")
            .parameterizedBy(String::class.asTypeName())
    )
        .mutable()
        .build()


    val requestClass = TypeSpec.classBuilder("${operation.name.capitalize()}Request")
        .addModifiers(KModifier.DATA)
        .getConstructor(params)


    if (validation != null)
        requestClass
            .addProperty(resultProp)
            .addInitializerBlock(initBlock.build())


//    val validation = getValidationCompObj(params)
    if (validation != null)
        requestClass.addType(validation)


    return requestClass.build()
}


fun getRequestType(parameters: List<Parameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .getConstructor(parameters)
        .build()
}


private fun getValidationCompObj(params: List<Parameter>): TypeSpec? {
    var hasValidations = false
    val validation = TypeSpec.companionObjectBuilder()

    params.map {
        if (!it.validations.isNullOrEmpty()) {
            hasValidations = true
            var initializer = ""
            it.validations.forEach { initializer += "\"${it}\"," }
            validation.addProperty(
                PropertySpec.builder(
                    "valid${it.name.capitalize()}",
                    Set::class.parameterizedBy(String::class)
                )
                    .initializer("setOf($initializer)")
                    .build()
            )
        }
    }

    return validation.build().takeIf { hasValidations }
}

/**
 * Get parameters when using requests with query strings or path parameter
 */
private fun getParameters(operation: DSLOperation): List<Parameter> {
    val params: MutableList<Parameter> = mutableListOf()

    operation.parameters?.forEach {
        val typeName: TypeName
        if (it.subType != null) {
            typeName = it.type.kotlinType.parameterizedBy(it.subType.kotlinType)
        } else
            typeName = it.type.kotlinType

        val visibility = if (it.`in` == In.QUERY) {
            Visibility.PRIVATE
        } else {
            Visibility.PUBLIC
        }

        params.add(
            Parameter(
                it.name,
                typeName.nullable(),
                visibility,
                it.enum?.map { it.toString() }
            )
        )
    }
    return params
}
