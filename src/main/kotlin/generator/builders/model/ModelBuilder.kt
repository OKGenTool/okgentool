package generator.builders.model

import com.squareup.kotlinpoet.FileSpec
import datamodel.Schema
import generator.builders.model.utils.createDataClassComponent
import generator.builders.model.utils.createSealedClassComponent
import generator.writeFile
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ModelBuilder.kt")

fun buildModel(schemas: List<Schema>) {
    logger.info("Start building model")
    for (schema in schemas) {
        val fileSpec = createModelComponent(schema, schemas)
        writeFile(fileSpec)
    }
}

fun createModelComponent(schema: Schema, schemas: List<Schema>): FileSpec {
    if (schema.superClassChildSchemaNames.isNotEmpty()) {
        return createSealedClassComponent(schema, schemas)
    }
    return createDataClassComponent(schema, schemas)
}
