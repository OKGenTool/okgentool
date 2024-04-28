package generator.builders

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import parser.components
import java.nio.file.Paths

fun buildModel(basePath: String) {
    for (component in components)
        createModelComponent(component, basePath)
}

private fun createModelComponent(component: Component, basePath: String){
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

    val fileSpec = FileSpec.builder("gen.routing.models", className)
        .addType(classBuilder.build())
        .build()

    val modelsDirectory = Paths.get(basePath).toFile()
    fileSpec.writeTo(modelsDirectory)
}

private fun getPropertyType(property: ComponentProperties): TypeName {
    return when {
        property.schemaName.isNotEmpty() ->{
            val relatedComponent = components.find { it.schemaName == property.schemaName }
            ClassName("gen.routing.models", relatedComponent!!.simplifiedName)
        }

        (property.dataType == DataType.ARRAY) && (property.arrayItemsType != null) ->
            property.dataType.kotlinType.parameterizedBy(property.arrayItemsType.kotlinType)

        (property.dataType == DataType.ARRAY) && !(property.arrayItemsSchemaName.isNullOrBlank()) -> {
            val relatedComponent = components.find { it.schemaName == property.arrayItemsSchemaName }
            property.dataType.kotlinType.parameterizedBy(ClassName("gen.routing.models", relatedComponent!!.simplifiedName))
        }

        else ->
            if (property.required) property.dataType!!.kotlinType
            else property.dataType!!.kotlinType.copy(nullable = true)
    }
}
