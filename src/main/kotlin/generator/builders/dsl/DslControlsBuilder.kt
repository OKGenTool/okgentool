package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("DslControlsBuilder.kt")

const val DSL_CONTROLS_CAP = "DslControls"
const val DSL_CONTROLS = "dslControls"

fun buildDslControls(destinationPath: String) {
    // Define the dslControls property
    val apiOperationsProperty = PropertySpec.builder(
        DSL_CONTROLS,
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
            val found = ${DSL_CONTROLS}.find { it == operation }
            if (found == null) ${DSL_CONTROLS}.add(operation)
            else throw RuntimeException("This operation was already created: $%L")
        """.trimIndent(),
            "operation"
        )
        .build()

    // Define the DslControls class
    val apiOperationsClass = TypeSpec.classBuilder(DSL_CONTROLS_CAP)
        .addProperty(apiOperationsProperty)
        .addFunction(addOperationFunSpec)
        .build()

    // Define the Kotlin file
    val fileSpec = FileSpec.builder(Packages.DSL, DSL_CONTROLS_CAP)
        .addType(apiOperationsClass)
        .build()

    writeFile(fileSpec, destinationPath)
}