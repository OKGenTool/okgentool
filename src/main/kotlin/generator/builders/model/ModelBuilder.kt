package generator.builders.model

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.builders.model.utils.createSealedClassComponent
import generator.builders.model.utils.createDataClassComponent
import generator.model.Packages
import kotlinx.serialization.Serializable
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

//fun getCompanionObjectBuilder(component: Component): TypeSpec? {
//    val properties = mutableListOf<PropertySpec>()
//    properties.addAll(getCompanionProperties(component))
//
//    if (component.superClassChildComponents.isNotEmpty()) {
//        component.superClassChildComponents.forEach { superClassChildComponent ->
//            properties.addAll(getCompanionProperties(superClassChildComponent))
//        }
//    }
//
//    if (properties.isEmpty()) {
//        return null
//    }
//
//    return TypeSpec.companionObjectBuilder()
//        .addProperties(properties)
//        .build()
//}

//fun getCompanionProperties(component: Component): List<PropertySpec> {
//    val properties = mutableListOf<PropertySpec>()
//
//    component.parameters.forEach {property ->
//        if (property.isEnum && property.values.size == 1 && property.dataType != null) {
//            properties.add(
//                PropertySpec.builder(
//                    property.name,
//                    property.dataType.kotlinType,
//                    KModifier.CONST
//                )
//                    .initializer(
//                        getInitializerForType(property.dataType),
//                        property.values.first()
//                    )
//                    .build()
//            )
//        }
//    }
//
//    return properties
//}
