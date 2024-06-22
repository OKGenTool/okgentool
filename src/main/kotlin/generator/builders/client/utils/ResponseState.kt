package generator.builders.client.utils

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import generator.model.Packages

fun createResponseStateFile(): FileSpec {
    val fileSpec = FileSpec.builder(Packages.CLIENT, "ResponseState")
    fileSpec.addType(createResponseStateType())
    fileSpec.addFunction(createResponseStateFunction())

    return fileSpec.build()
}

private fun createResponseStateType(): TypeSpec {
    return TypeSpec.classBuilder("ResponseState")
        .addModifiers(KModifier.DATA)
        .addTypeVariable(TypeVariableName("T"))
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("isSuccess", Boolean::class.asTypeName().copy(nullable = true))
                .addParameter("isError", Boolean::class.asTypeName().copy(nullable = true))
                .addParameter("response", TypeVariableName("T").copy(nullable = true))
                .addParameter("statusCode", Int::class.asTypeName().copy(nullable = true))
                .build()
        )
        .addProperty(
            PropertySpec.builder("isSuccess", Boolean::class.asTypeName().copy(nullable = true))
                .initializer("isSuccess")
                .build()
        )
        .addProperty(
            PropertySpec.builder("isError", Boolean::class.asTypeName().copy(nullable = true))
                .initializer("isError")
                .build()
        )
        .addProperty(
            PropertySpec.builder("response", TypeVariableName("T").copy(nullable = true))
                .initializer("response")
                .build()
        )
        .addProperty(
            PropertySpec.builder("statusCode", Int::class.asTypeName().copy(nullable = true))
                .initializer("statusCode")
                .build()
        )
        .build()
}

private fun createResponseStateFunction(): FunSpec {
    return FunSpec.builder("getResponseState")
        .addTypeVariable(TypeVariableName("T"))
        .addParameter("response", TypeVariableName("T").copy(nullable = true))
        .addParameter("statusCode", Int::class.asTypeName().copy(nullable = true))
        .returns(ClassName(Packages.CLIENT, "ResponseState").parameterizedBy(TypeVariableName("T")))
        .addCode(
            """
            |if (statusCode in 200..299) {
            |    return ResponseState(
            |        isSuccess = true,
            |        isError = false,
            |        response = response,
            |        statusCode = statusCode
            |    )
            |}
            |
            |return ResponseState(
            |    isSuccess = false,
            |    isError = true,
            |    response = response,
            |    statusCode = statusCode
            |)
            |""".trimMargin()
        )
        .build()
}
