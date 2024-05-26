package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.DSLOperation
import generator.capitalize
import generator.model.GenParameter
import generator.model.Packages
import generator.model.Visibility

/**
 * Build request class when using query parameters
 */
fun getRequestQueryParam(operation: DSLOperation): TypeSpec {
    val params = getQueryParameters(operation)
    val validation = getValidationCompObj(params)

    //TODO must implement multiple validations. One for each query string
    val firstParameter = operation.parameters?.first()
    val paramName = firstParameter?.name
    val paramValidation = "valid${firstParameter?.name?.capitalize()}"

    // Create the init block
    val initBlock = CodeBlock.builder()
    if (validation != null) {
        initBlock.beginControlFlow("if ($paramName in $paramValidation)")
            .addStatement("result = ReadRequestResult.Success(status!!)")
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


fun getRequestType(genParameters: List<GenParameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .getConstructor(genParameters)
        .build()
}


private fun getValidationCompObj(params: List<GenParameter>): TypeSpec? {
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

fun getQueryParameters(operation: DSLOperation): List<GenParameter> {
    val params: MutableList<GenParameter> = mutableListOf()

    operation.parameters?.forEach {
        val typeName: TypeName
        if (it.subType != null) {
            typeName = it.type.kotlinType.parameterizedBy(it.subType.kotlinType)
        } else
            typeName = it.type.kotlinType

        params.add(
            GenParameter(
                it.name,
                typeName.copy(nullable = true),
                Visibility.PRIVATE,
                it.enum?.map { it.toString() }
            )
        )
    }
    return params
}
