package generator.builders.defaultRouting

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import datamodel.Schema
import generator.builders.defaultRouting.utils.getExampleObject
import generator.builders.defaultRouting.utils.getNeededImports
import generator.capitalize
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

        for (import in getNeededImports(schema)) {
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
    }

    routingFileSpec.addFunction(defaultRoutingFunction.build())
    writeFile(routingFileSpec.build())
}

fun createOperationStatement(operation: DSLOperation, schemas: List<Schema>): CodeBlock {
    val code = CodeBlock.builder()
    code.add("route.${operation.name} {\n")
    code.add("\t\ttry {\n")

    if (operation.parameters != null) {
        for (parameter in operation.parameters) {
            code.add("\t\t\tval ${parameter.name} = request.${parameter.name}\n")
        }
    }

    if (operation.requestBody != null) {
        // TODO: Add support for request body
//        code.add("\t\t\tval ${operation.} = request.body\n")
    }

    code.add("\t\t\tif (example${operation.name.capitalize()} != null) {\n")
    code.add("\t\t\t\tcall.respond(example${operation.name.capitalize()})\n")
    code.add("\t\t\t} else {\n")
    code.add("\t\t\tunsafe.notImplemented()\n")
    code.add("\t\t\t}\n")
    code.add("\t\t} catch (e: Exception) {\n")
    code.add("\t\t\t\tunsafe.error(e)\n")
    code.add("\t\t}\n")

    code.add("}\n\n")

    return code.build()
}
