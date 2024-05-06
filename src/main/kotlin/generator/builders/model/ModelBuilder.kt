package generator.builders.model

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import datamodel.getInitializerForType
import generator.model.Packages
import io.swagger.v3.oas.models.Components
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
    return createSimpleComponent(component, components)
}

fun createSealedClassComponent(component: Component, components: List<Component>): FileSpec {
    val sealedClass = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(Serializable::class)
        .build()

    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(sealedClass)
        .addTypes(getSubclasses(component, components, component.simplifiedName))
        .build()
}

fun getSubclasses(superClassComponent: Component, components: List<Component>, superclassName: String): List<TypeSpec> {
    val subclasses = mutableListOf<TypeSpec>()

    for (component in superClassComponent.superClassChildComponents) {
        subclasses.add(
            getDataClassBuilder(
                component,
                components,
                ClassName(Packages.MODEL, superclassName),
            )
        )
        subclasses.addAll(getEnumBuilders(component))
    }

    return subclasses
}

fun createSimpleComponent(component: Component, components: List<Component>): FileSpec {
    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(getDataClassBuilder(component, components))
        .addTypes(getEnumBuilders(component))
        .build()
}

fun getEnumBuilders(component: Component): List<TypeSpec> {
    val enums = component.parameters.filter { it.isEnum }
    val enumBuilders = mutableListOf<TypeSpec>()
    component.parameters.forEach { property ->
        if (property.values.isNotEmpty()) {
            enums.forEach { enum ->
                val enumBuilder = TypeSpec.enumBuilder(component.simplifiedName + enum.name.capitalize())
                enum.values.forEach { enumValue ->
                    enumBuilder.addEnumConstant(enumValue)
                }
                enumBuilders.add(enumBuilder.build())
            }
        }
    }

    return enumBuilders
}

fun getDataClassBuilder(
    component: Component,
    components: List<Component>,
    superclassName: ClassName? = null,
    companionObject: TypeSpec? = null,
): TypeSpec {
    val properties = mutableListOf<PropertySpec>()

    for (property in component.parameters) {
        val propertyType = getPropertyType(property, component.simplifiedName, components)

        val propertySpec = PropertySpec.builder(property.name, propertyType)
            .initializer(property.name)
            .build()
        properties.add(propertySpec)
    }

    val primaryConstructor = FunSpec.constructorBuilder()
        .addParameters(properties.map { ParameterSpec(it.name, it.type) })
        .build()

    val dataClassBuilder = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(primaryConstructor)
        .addProperties(properties)
        .addAnnotation(Serializable::class)

    if (superclassName != null) {
        dataClassBuilder.superclass(superclassName)
    }

    if (companionObject != null) {
        dataClassBuilder.addType(companionObject)
    }

    return dataClassBuilder.build()
}

fun getPropertyType(property: ComponentProperties, componentName: String, components: List<Component>): TypeName {
    return when {
        property.isEnum && property.values.isNotEmpty() -> ClassName(
            Packages.MODEL,
            componentName + property.name.capitalize()
        )

        property.schemaName.isNotEmpty() -> {
            val relatedComponent = components.find { it.schemaName == property.schemaName }
            ClassName(Packages.MODEL, relatedComponent!!.simplifiedName)
        }

        (property.dataType == DataType.ARRAY) && (property.arrayItemsType != null) ->
            property.dataType.kotlinType.parameterizedBy(property.arrayItemsType.kotlinType)

        (property.dataType == DataType.ARRAY) && !(property.arrayItemsSchemaName.isNullOrBlank()) -> {
            val relatedComponent = components.find { it.schemaName == property.arrayItemsSchemaName }
            property.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent!!.simplifiedName
                )
            )
        }

        else ->
            if (property.required) property.dataType!!.kotlinType
            else property.dataType!!.kotlinType.copy(nullable = true)
    }
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
