package generator.builders.routing.plugins

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import generator.model.Imports.Companion.addCustomImport
import generator.model.Imports.*
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory

private const val SERIALIZATION = "configureGenSerialization"
private const val SERIALIZATION_FILE = "Serialization.kt"

private val logger = LoggerFactory.getLogger("SerializationBuilder")

fun buildSerialization(destinationPath: String) {
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
        .addCustomImport(KTOR_APPLICATION_APPLICATION)
        .addCustomImport(KTOR_APPLICATION_INSTALL)
        .addCustomImport(KTOR_CONTENT_NEGOTIATION)
        .addCustomImport(KTOR_SERIALIZATION_JSON)
        .addCustomImport(KTOR_SERIALIZATION_XML)
        .build()

    writeFile(file, destinationPath)
}