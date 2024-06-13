package generator.builders.defaultRouting

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import datamodel.Schema
import generator.builders.defaultRouting.utils.createExampleObject
import generator.builders.defaultRouting.utils.getNeededImports
import generator.model.Packages
import generator.writeFile
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter

private val logger = LoggerFactory.getLogger("DefaultRoutingBuilder.kt")

fun buildDefaultRouting(operations: List<DSLOperation>, schemas: List<Schema>) {
    logger.info("Start building default routing")

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
        val properties = createExampleObject(schema, schemas)
        for (property in properties) {
            examplesFileSpec.addProperty(property)
        }

        for (import in getNeededImports(schema)) {
            examplesFileSpec.addImport(import.first, import.second)
        }

    }
    writeFile(examplesFileSpec.build())

//    for (operation in operations) {
//        val fileSpec = createDefaultRoutingComponent(operation, schemas)
//        writeFile(fileSpec)
//    }
}

// TODO: Implement the function that creates a default routing component
fun createDefaultRoutingComponent(operation: DSLOperation, schemas: List<Schema>): FileSpec {
    return FileSpec.builder(Packages.DEFAULT_ROUTING, operation.name + "DefaultRouting")
        .build()
}
