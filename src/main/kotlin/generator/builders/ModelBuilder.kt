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
        val fileSpec = createModelComponent(component, basePath)
        writeFile(fileSpec, basePath)
    }
}

private fun createModelComponent(component: Component, basePath: String): FileSpec {
    val className = component.simplifiedName

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

    val classBuilder = TypeSpec.classBuilder(className)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(primaryConstructor)
        .addProperties(properties)

    val fileSpec = FileSpec.builder(Packages.MODEL, className)
        .addType(classBuilder.build())
        .build()

    return fileSpec
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
