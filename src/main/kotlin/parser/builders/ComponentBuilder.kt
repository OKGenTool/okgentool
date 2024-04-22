package parser.builders

import parser.model.Component
import parser.model.ComponentProperties
import parser.model.DataType
import parser.openAPI

fun getComponents(): List<Component> {
    val components = openAPI.components.schemas ?: return emptyList()
    val res = mutableListOf<Component>()

    for (component in components) {
        val schema = component.value
        val requiredProperties = schema.required ?: emptyList()

        val properties = mutableListOf<ComponentProperties>()

        for (parameter in schema.properties) {
            val name = parameter.key
            val dataType = DataType.fromString(parameter.value.type ?: "", parameter.value.format ?: "")
            val required = requiredProperties.contains(name)
            val schemaName = parameter.value.`$ref` ?: ""

            properties.add(ComponentProperties(name, dataType, required, schemaName))
        }
        res.add(Component("#/components/schemas/" + component.key, properties))
    }
    return res
}