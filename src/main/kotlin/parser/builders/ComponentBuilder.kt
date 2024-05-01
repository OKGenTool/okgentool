package parser.builders

import cli.logger
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import io.swagger.v3.oas.models.media.Schema
import parser.openAPI

fun getComponents(): List<Component> {
    val components = openAPI.components.schemas ?: return emptyList()
    val res = mutableListOf<Component>()

    for (component in components) {
        val schema = component.value
        val requiredProperties = schema.required ?: emptyList()
        val properties = getProperties(schema, requiredProperties)
        val oneOfSchemaNames = getOneOfSchemaNames(schema)
        val schemaName = "#/components/schemas/" + component.key
        logger().info("Parsing component: $schemaName")
        res.add(Component(schemaName, properties, component.key.capitalize(), oneOfSchemaNames))
    }

    return checkChildren(removeDuplicates(res))
}

private fun removeDuplicates(components: MutableList<Component>): MutableList<Component> {
    val sealedComponents = components.filter { it.superClassChildSchemaNames.isNotEmpty() }

    for (component in sealedComponents) {
        val oneOfSchemaNames = component.superClassChildSchemaNames
        for (schemaName in oneOfSchemaNames) {
            val relatedComponent = components.find { it.schemaName == schemaName } ?: continue
            component.superClassChildComponents.add(relatedComponent)
            components.remove(relatedComponent)
        }
    }

    return components
}

private fun getOneOfSchemaNames(schema: Schema<Any>?): List<String> {
    val oneOfSchemaNames = mutableListOf<String>()

    if (schema != null && schema.oneOf != null) {
        for (oneOf in schema.oneOf) {
            oneOfSchemaNames.add(oneOf.`$ref` ?: "")
        }
    }

    return oneOfSchemaNames
}

private fun checkChildren(components: MutableList<Component>): List<Component> {
    val simplifiedNames = mutableListOf<Pair<String, String>>()

    for (component in components) {
        simplifiedNames.add(Pair(component.simplifiedName, component.simplifiedName.split("_").first()))
    }

    for ((key, value) in simplifiedNames) {
        var parent = components.find { it.simplifiedName == value }
        val child = components.find { it.simplifiedName == key }!!
        if (parent != null && key != value && parent.parameters == child.parameters) {
            parent.schemaNameChildren.add(child.schemaName)
            parent.schemaNameChildren.addAll(child.schemaNameChildren)
            components.remove(child)
            continue
        }

        parent = components.find { it.parameters == child.parameters && it != child }
        if (parent != null && ((key.contains("Body") && key.contains(parent.simplifiedName)) || key.contains("Response")) ) {
            parent.schemaNameChildren.add(child.schemaName)
            parent.schemaNameChildren.addAll(child.schemaNameChildren)
            components.remove(child)
        }
    }

    return components
}

private fun getProperties(schema: Schema<Any>?, requiredProperties: List<String>): List<ComponentProperties> {
    val properties = mutableListOf<ComponentProperties>()

    if (schema != null && schema.properties != null) {
        for (parameter in schema.properties) {
            val name = parameter.key
            val dataType = DataType.fromString(parameter.value.type ?: "", parameter.value.format ?: "")
            val required = requiredProperties.contains(name)
            val schemaName = parameter.value.`$ref` ?: ""

            if (dataType == DataType.ARRAY) {
                val arrayItems = parameter.value.items
                val arrayItemsType = DataType.fromString(arrayItems?.type ?: "", arrayItems?.format ?: "")
                val arrayItemsSchemaName = arrayItems?.`$ref` ?: ""
                properties.add(ComponentProperties(name, dataType, required, schemaName, arrayItemsType, arrayItemsSchemaName))
                continue
            }

            properties.add(ComponentProperties(name, dataType, required, schemaName))
        }
    }

    return properties
}
