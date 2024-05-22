package parser.builders

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType
import io.swagger.v3.oas.models.media.Schema
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("ComponentBuilder.kt")

fun getComponents(): List<Component> {
    logger.info("Reading components")
    val components = openAPI.components.schemas ?: return emptyList()
    val res = mutableListOf<Component>()

    for (component in components) {
        val schema = component.value
        val requiredProperties = schema.required ?: emptyList()
        val properties = getProperties(schema, requiredProperties)
        val oneOfSchemaNames = getOneOfSchemaNames(schema)
        val schemaName = "#/components/schemas/" + component.key
        logger.info("Parsing component: $schemaName")
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
        if (parent != null && ((key.contains("Body") && key.contains(parent.simplifiedName)) || key.contains("Response"))) {
            parent.schemaNameChildren.add(child.schemaName)
            parent.schemaNameChildren.addAll(child.schemaNameChildren)
            components.remove(child)
        }
    }

    return components
}

private fun getProperties(schema: Schema<Any>?, requiredProperties: List<String>): List<ComponentProperty> {
    val properties = mutableListOf<ComponentProperty>()

    if (schema != null && schema.properties != null) {
        for (parameter in schema.properties) {
            val name = parameter.key
            val dataType = DataType.fromString(parameter.value.type ?: "", parameter.value.format ?: "")
            val required = requiredProperties.contains(name)
            val schemaName = parameter.value.`$ref` ?: ""
            val values = parameter.value.enum?.map { it.toString() } ?: emptyList()

            if (dataType == DataType.ARRAY) {
                val arrayItems = parameter.value
                val arrayIndex = 0
                val minItems = parameter.value.minItems
                val maxItems = parameter.value.maxItems
                val uniqueItems = parameter.value.uniqueItems ?: false

                properties.add(
                    getArrayProperties(
                        arrayItems = arrayItems,
                        name = name,
                        dataType = dataType,
                        required = required,
                        schemaName = schemaName,
                        values = values,
                        arrayIndex = arrayIndex,
                        minItems = minItems,
                        maxItems = maxItems,
                        uniqueItems = uniqueItems
                    )
                )
                continue
            }

            val minimum = parameter.value.minimum?.toInt()
            val maximum = parameter.value.maximum?.toInt()
            val exclusiveMinimum = parameter.value.exclusiveMinimum ?: false
            val exclusiveMaximum = parameter.value.exclusiveMaximum ?: false
            val multipleOf = parameter.value.multipleOf?.toInt()
            val pattern = parameter.value.pattern
            val minLength = parameter.value.minLength
            val maxLength = parameter.value.maxLength

            properties.add(
                ComponentProperty(
                    name = name,
                    dataType = dataType,
                    required = required,
                    schemaName = schemaName,
                    isEnum = values.isNotEmpty(),
                    values = values,
                    minimum = minimum,
                    maximum = maximum,
                    exclusiveMinimum = exclusiveMinimum,
                    exclusiveMaximum = exclusiveMaximum,
                    multipleOf = multipleOf,
                    minLength = minLength,
                    maxLength = maxLength,
                    pattern = pattern
                )
            )
        }
    }

    return properties
}

private fun getArrayProperties(
    arrayItems: Schema<*>?,
    name: String,
    dataType: DataType,
    required: Boolean,
    schemaName: String,
    values: List<String>,
    arrayIndex: Int,
    minItems: Int?,
    maxItems: Int?,
    uniqueItems: Boolean = false
): ComponentProperty {
    val childArrayItems = arrayItems?.items
    val arrayItemsType = DataType.fromString(arrayItems?.items?.type ?: "", arrayItems?.items?.format ?: "")
    val arrayItemsSchemaName = arrayItems?.`$ref` ?: ""
    val childUniqueItems = arrayItems?.uniqueItems ?: false
    val childMinItems = arrayItems?.minItems
    val childMaxItems = arrayItems?.maxItems
    val childArrayIndex = arrayIndex + 1

    if (arrayItemsType != DataType.ARRAY) {
        return ComponentProperty(
            name = name,
            dataType = dataType,
            required = required,
            schemaName = schemaName,
            isEnum = values.isNotEmpty(),
            arrayItemsType = arrayItemsType,
            arrayItemsSchemaName = arrayItemsSchemaName,
            values = values,
            minItems = minItems,
            maxItems = maxItems,
            arrayIndex = arrayIndex,
            uniqueItems = uniqueItems
        )
    }

    return ComponentProperty(
        name = name,
        dataType = dataType,
        required = required,
        schemaName = schemaName,
        isEnum = values.isNotEmpty(),
        arrayItemsType = arrayItemsType,
        arrayItemsSchemaName = arrayItemsSchemaName,
        values = values,
        minItems = minItems,
        maxItems = maxItems,
        arrayIndex = arrayIndex,
        uniqueItems = uniqueItems,
        arrayProperties = getArrayProperties(
            arrayItems = childArrayItems,
            name = name,
            dataType = dataType,
            required = required,
            schemaName = schemaName,
            values = values,
            minItems = childMinItems,
            maxItems = childMaxItems,
            arrayIndex = childArrayIndex,
            uniqueItems = childUniqueItems
        )
    )
}
