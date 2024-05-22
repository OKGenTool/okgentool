package generator.builders.model

import com.squareup.kotlinpoet.*
import datamodel.Component
import generator.builders.model.utils.createSealedClassComponent
import generator.builders.model.utils.createDataClassComponent
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("ModelBuilder.kt")

fun buildModel(components: List<Component>, basePath: String) {
    logger.info("Start building model")
    for (component in components) {
        val fileSpec = createModelComponent(component, components)
        writeFile(fileSpec, basePath)
    }
}

fun createModelComponent(component: Component, components: List<Component>): FileSpec {
    if (component.superClassChildSchemaNames.isNotEmpty()) {
        return createSealedClassComponent(component, components)
    }
    return createDataClassComponent(component, components)
}
