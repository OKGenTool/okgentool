package generator.builders

import cli.logger
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import datamodel.getInitializerForType
import generator.model.Packages
import output.writeFile
import parser.components

fun buildModel(basePath: String) {
    logger().info("Start building model")
    for (component in components) {
        val fileSpec = createModelComponent(component)
        writeFile(fileSpec, basePath)
    }
}

private fun createModelComponent(component: Component): FileSpec {
    if (component.superClassChildSchemaNames.isNotEmpty()) {
        return createSealedClassComponent(component)
    }
    return createSimpleComponent(component)
}

private fun createSealedClassComponent(component: Component): FileSpec {
    val sealedClass = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.SEALED)
        .build()

    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(sealedClass)
        .addTypes(getSubclasses(component))
        .build()
}

private fun getSubclasses(superClassComponent: Component): List<TypeSpec> {
    val subclasses = mutableListOf<TypeSpec>()

    for (component in superClassComponent.superClassChildComponents) {
        subclasses.add(
            getDataClassBuilder(
                component,
                ClassName(Packages.MODEL, component.simplifiedName),
                getCompanionObjectBuilder(component)
            )
        )
        subclasses.addAll(getEnumBuilders(component))
    }

    return subclasses
}

private fun createSimpleComponent(component: Component): FileSpec {
    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(getDataClassBuilder(component, companionObject = getCompanionObjectBuilder(component)))
        .addTypes(getEnumBuilders(component))
        .build()
}

private fun getCompanionObjectBuilder(component: Component): TypeSpec? {
    val properties = mutableListOf<PropertySpec>()
    properties.addAll(getCompanionProperties(component))

    if (component.superClassChildComponents.isNotEmpty()) {
        component.superClassChildComponents.forEach { superClassChildComponent ->
            properties.addAll(getCompanionProperties(superClassChildComponent))
        }
    }

    if (properties.isEmpty()) {
        return null
    }

    return TypeSpec.companionObjectBuilder()
        .addProperties(properties)
        .build()
}

private fun getCompanionProperties(component: Component): List<PropertySpec> {
    val properties = mutableListOf<PropertySpec>()

    component.parameters.forEach {property ->
        if (property.isEnum && property.values.size == 1 && property.dataType != null) {
            properties.add(
                PropertySpec.builder(
                    property.name,
                    property.dataType.kotlinType,
                    KModifier.CONST
                )
                    .initializer(
                        getInitializerForType(property.dataType),
                        property.values.first()
                    )
                    .build()
            )
        }
    }

    return properties
}

private fun getEnumBuilders(component: Component): List<TypeSpec> {
    val enums = component.parameters.filter { it.isEnum }
    val enumBuilders = mutableListOf<TypeSpec>()
    component.parameters.forEach { property ->
        if (property.values.size > 1) {
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

private fun getDataClassBuilder(
    component: Component,
    superclassName: ClassName? = null,
    companionObject: TypeSpec? = null
): TypeSpec {
    val properties = mutableListOf<PropertySpec>()

    for (property in component.parameters) {
        val propertyType = getPropertyType(property, component.simplifiedName) ?: continue

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

    if (superclassName != null) {
        dataClassBuilder.superclass(superclassName)
    }

    if (companionObject != null) {
        dataClassBuilder.addType(companionObject)
    }

    return dataClassBuilder.build()
}

private fun getPropertyType(property: ComponentProperties, componentName: String): TypeName? {
    return when {
        property.isEnum && property.values.size > 1 -> ClassName(Packages.MODEL, componentName + property.name.capitalize())

        property.isEnum && property.values.size == 1 -> null

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
