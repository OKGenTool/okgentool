package parser.builders

import io.swagger.v3.oas.models.media.Schema
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import parser.openAPI

fun getComponents(): List<Component> {
    val components = openAPI.components.schemas ?: return emptyList()
    val res = mutableListOf<Component>()

    for (component in components) {
        val schema = component.value
        val requiredProperties = schema.required ?: emptyList()
        val properties = getProperties(schema, requiredProperties)
        val schemaName = "#/components/schemas/" + component.key
        res.add(Component(schemaName, properties, component.key.capitalize()))
    }
    return checkSons(res)
}

fun checkSons(components: MutableList<Component>): List<Component> {
    val simplifiedNames = mutableListOf<Pair<String, String>>()

    for (component in components) {
        simplifiedNames.add(Pair(component.simplifiedName, component.simplifiedName.split("_").first()))
    }

    for ((key, value) in simplifiedNames) {
        var father = components.find { it.simplifiedName == value }
        val son = components.find { it.simplifiedName == key }!!
        if (father != null && key != value && father.parameters == son.parameters) {
            father.schemaNameSons.add(son.schemaName)
            father.schemaNameSons.addAll(son.schemaNameSons)
            components.remove(son)
            continue
        }

        father = components.find { it.parameters == son.parameters && it != son }
        if (father != null && ((key.contains("Body") && key.contains(father.simplifiedName)) || key.contains("Response")) ) {
            father.schemaNameSons.add(son.schemaName)
            father.schemaNameSons.addAll(son.schemaNameSons)
            components.remove(son)
        }
    }

    return components
}

private fun getProperties(schema: Schema<Any>?, requiredProperties: List<String>): List<ComponentProperties> {
    val properties = mutableListOf<ComponentProperties>()

    if (schema != null) {
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
