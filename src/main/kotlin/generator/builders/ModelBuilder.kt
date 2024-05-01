package generator.builders

import cli.logger
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
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
    val subclasses = getSubclasses(component)

    val sealedClass = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.SEALED)
        .build()

    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(sealedClass)
        .addTypes(subclasses)
        .build()
}

private fun getSubclasses(superClassComponent: Component): List<TypeSpec> {
    val subclasses = mutableListOf<TypeSpec>()

    for (component in superClassComponent.superClassChildComponents) {
        subclasses.add(getDataClassBuilder(component, ClassName(Packages.MODEL, component.simplifiedName)))
    }

    return subclasses
}

private fun createSimpleComponent(component: Component): FileSpec {
    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(getDataClassBuilder(component))
        .build()
}

private fun getDataClassBuilder(component: Component, superclassName: ClassName? = null): TypeSpec {
    val properties = mutableListOf<PropertySpec>()

    for (property in component.parameters) {
        val propertyType = getPropertyType(property)
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

    return dataClassBuilder.build()
}

private fun getPropertyType(property: ComponentProperties): TypeName {
    return when {
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
