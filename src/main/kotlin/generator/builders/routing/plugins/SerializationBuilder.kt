package generator.builders.routing.plugins

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import generator.model.Packages
import org.slf4j.LoggerFactory
import output.writeFile

private const val SERIALIZATION = "configureGenSerialization"
private const val SERIALIZATION_FILE = "Serialization.kt"

private val logger = LoggerFactory.getLogger("SerializationBuilder")

fun buildSerialization(basePath: String){
    logger.info("Generating $SERIALIZATION_FILE")

    // Create the function
    val functionSpec = FunSpec.builder(SERIALIZATION)
        .receiver(ClassName("io.ktor.server.application", "Application"))
        .addCode(
            """
            install(ContentNegotiation) {
                json()
                xml()
            }
            """.trimIndent()
        )
        .build()

    // Create the Kotlin file
    val file = FileSpec.builder(Packages.PLUGINS, SERIALIZATION_FILE)
        .addFunction(functionSpec)
        .addImport("io.ktor.server.application", "Application","install")
        .addImport("io.ktor.server.plugins.contentnegotiation", "ContentNegotiation")
        .addImport("io.ktor.serialization.kotlinx.json", "json")
        .addImport("io.ktor.serialization.kotlinx.xml", "xml")
        .build()

    writeFile(file, basePath)
}