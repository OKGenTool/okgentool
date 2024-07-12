package generator.builders.client.utils

import com.squareup.kotlinpoet.*
import generator.model.Packages

fun createResponseStateFile(): FileSpec {
    val fileSpec = FileSpec.builder(Packages.CLIENT, "ResponseState")
    fileSpec.addType(createResponseStateType())

    return fileSpec.build()
}

private fun createResponseStateType(): TypeSpec {
    return TypeSpec.classBuilder("ResponseState")
        .addModifiers(KModifier.DATA)
        .addTypeVariable(TypeVariableName("T"))
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("responseBody", TypeVariableName("T").copy(nullable = true))
                .addParameter("statusCode", Int::class.asTypeName().copy(nullable = true))
                .build()
        )
        .addProperty(
            PropertySpec.builder("responseBody", TypeVariableName("T").copy(nullable = true))
                .initializer("responseBody")
                .build()
        )
        .addProperty(
            PropertySpec.builder("statusCode", Int::class.asTypeName().copy(nullable = true))
                .initializer("statusCode")
                .build()
        )
        .build()
}
