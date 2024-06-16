package generator.builders.defaultRouting

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import datamodel.Schema
import generator.builders.defaultRouting.utils.createOperationStatement
import generator.builders.defaultRouting.utils.getExampleObject
import generator.builders.defaultRouting.utils.getExampleImports
import generator.builders.defaultRouting.utils.getRoutingImports
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

private val logger = LoggerFactory.getLogger("DefaultRoutingBuilder.kt")

fun buildDefaultRouting(operations: List<DSLOperation>, schemas: List<Schema>) {
    logger.info("Start building default routing")

    writeDefaultRoutingExamplesFile(schemas)
    writeDefaultRoutingFile(operations, schemas)
}

private fun writeDefaultRoutingExamplesFile(schemas: List<Schema>) {
    val examplesFileSpec = FileSpec.builder(Packages.DEFAULT_ROUTING_EXAMPLES, "DefaultRoutingExamples")
        .addImport("java.time", "LocalDate")

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
    writeFile(examplesFileSpec.build())
}

private fun writeDefaultRoutingFile(operations: List<DSLOperation>, schemas: List<Schema>) {
    val routingFileSpec = FileSpec.builder(Packages.DEFAULT_ROUTING, "DefaultRouting")

    val defaultRoutingFunction = FunSpec.builder("defaultRouting")
        .receiver(ClassName(Packages.DSL, "OkGenDsl"))

    for (operation in operations) {
        val operationStatement = createOperationStatement(operation, schemas)
        defaultRoutingFunction.addCode(operationStatement)

        for (import in getRoutingImports(operation, schemas)) {
            routingFileSpec.addImport(import.first, import.second)
        }
    }

    routingFileSpec.addFunction(defaultRoutingFunction.build())
    writeFile(routingFileSpec.build())
}


