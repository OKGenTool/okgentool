package generator.builders.defaultRouting

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import datamodel.Schema
import generator.builders.defaultRouting.utils.createOperationStatement
import generator.builders.defaultRouting.utils.getExampleImports
import generator.builders.defaultRouting.utils.getExampleObject
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory
import parser.builders.notImplemented
import java.time.format.DateTimeFormatter

private val logger = LoggerFactory.getLogger("DefaultRoutingBuilder.kt")

fun buildDefaultRouting(operations: List<DSLOperation>, schemas: List<Schema>, destinationPath: String) {
    logger.info("Start building default routing")

    writeDefaultRoutingExamplesFile(schemas, destinationPath)
    writeDefaultRoutingFile(operations, schemas, destinationPath)
}

private fun writeDefaultRoutingExamplesFile(schemas: List<Schema>, destinationPath: String) {
    val examplesFileSpec = FileSpec.builder(Packages.ROUTES, "DefaultRoutingExamples")
        .addImport("java.time", "LocalDate")
        .addImport("java.time", "LocalDateTime")

    val dataTimeFormaterProperty = PropertySpec.builder("dateTimeFormatter", DateTimeFormatter::class)
        .initializer("%T.ISO_LOCAL_DATE_TIME", DateTimeFormatter::class)
        .addModifiers(KModifier.PRIVATE)
        .build()

    examplesFileSpec.addProperty(dataTimeFormaterProperty)

    val localDateFormaterProperty = PropertySpec.builder("localDateFormatter", DateTimeFormatter::class)
        .initializer("%T.ISO_LOCAL_DATE", DateTimeFormatter::class)
        .addModifiers(KModifier.PRIVATE)
        .build()

    examplesFileSpec.addProperty(localDateFormaterProperty)

    for (schema in schemas) {
        val properties = getExampleObject(schema)
        for (property in properties) {
            examplesFileSpec.addProperty(property)
        }

        for (import in getExampleImports(schema)) {
            examplesFileSpec.addImport(import.first, import.second)
        }

    }
    writeFile(examplesFileSpec.build(), destinationPath)
}

private fun writeDefaultRoutingFile(operations: List<DSLOperation>, schemas: List<Schema>, destinationPath: String) {
    val routingFileSpec = FileSpec.builder(Packages.ROUTES, "DefaultRouting")

    val defaultRoutingFunction = FunSpec.builder("defaultRouting")
        .receiver(ClassName(Packages.DSL, "OkGenDsl"))

    for (operation in operations) {
        if (operation.name in notImplemented) continue

        val operationStatement = createOperationStatement(operation, schemas)
        defaultRoutingFunction.addCode(operationStatement)
    }

    routingFileSpec.addFunction(defaultRoutingFunction.build())
    writeFile(routingFileSpec.build(), destinationPath)
}


