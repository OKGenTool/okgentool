package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ApiOperationsBuilder.kt")

fun buildApiOperations() {
    // Define the apiOperations property
    val apiOperationsProperty = PropertySpec.builder(
        "apiOperations",
        ClassName("kotlin.collections", "MutableList")
            .parameterizedBy(ClassName("kotlin", "String"))
    )
        .addModifiers(KModifier.PRIVATE)
        .initializer("mutableListOf()")
        .build()

    // Define the addOperation function
    val addOperationFunSpec = FunSpec.builder("addOperation")
        .addParameter("operation", String::class)
        .addCode(
            """
            val found = apiOperations.find { it == operation }
            if (found == null) apiOperations.add(operation)
            else throw RuntimeException("This operation was already created: '$'operation")
        """.trimIndent()
        )
        .build()

    // Define the ApiOperations class
    val apiOperationsClass = TypeSpec.classBuilder("ApiOperations")
        .addProperty(apiOperationsProperty)
        .addFunction(addOperationFunSpec)
        .build()

    // Define the Kotlin file
    val fileSpec = FileSpec.builder(Packages.DSL, "ApiOperations")
        .addType(apiOperationsClass)
        .build()

    writeFile(fileSpec)
}