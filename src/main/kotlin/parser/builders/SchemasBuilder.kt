package parser.builders

import datamodel.*
import generator.capitalize
import io.swagger.v3.oas.models.media.Schema
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("SchemasBuilder.kt")

fun buildSchemas(): List<datamodel.Schema> {
    logger.info("Reading schemas")
    val schemas = openAPI.components.schemas ?: return emptyList()
    val res = mutableListOf<datamodel.Schema>()

    for (component in schemas) {
        val schema = component.value
        val requiredProperties = schema.required ?: emptyList()
        val parameters = getParameters(schema, requiredProperties)
        val oneOfSchemaNames = getOneOfSchemaNames(schema)
        val schemaName = "#/components/schemas/" + component.key
        logger.info("Parsing schema: $schemaName")
        res.add(datamodel.Schema(schemaName, parameters, component.key.capitalize(), oneOfSchemaNames))
    }
    return res
//    return checkChildren(removeDuplicates(res))
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

private fun getParameters(schema: Schema<Any>?, requiredProperties: List<String>): List<Parameter> {
    val properties = mutableListOf<Parameter>()

    if (schema != null && schema.properties != null) {
        for (parameter in schema.properties) {
            val name = parameter.key
            val dataType = DataType.fromString(parameter.value.type ?: "", parameter.value.format ?: "")
            val required = requiredProperties.contains(name) || parameter.value.nullable == false
            val schemaName = parameter.value.`$ref` ?: ""
            val example = parameter.value.example ?: ""
            val parameterProperties = getParameterProperties(parameter.value, dataType)

            properties.add(
                Parameter(
                    name = name,
                    dataType = dataType,
                    required = required,
                    schemaName = schemaName,
                    properties = parameterProperties,
                    example = example
                )
            )
            continue

//            val values = parameter.value.enum?.map { it.toString() } ?: emptyList()
//
//            if (dataType == DataType.ARRAY) {
//                val arrayItems = parameter.value
//                val arrayIndex = 0
//                val minItems = parameter.value.minItems
//                val maxItems = parameter.value.maxItems
//                val uniqueItems = parameter.value.uniqueItems ?: false
//
//                properties.add(
//                    getArrayProperties(
//                        arrayItems = arrayItems,
//                        name = name,
//                        dataType = dataType,
//                        required = required,
//                        schemaName = schemaName,
//                        values = values,
//                        arrayIndex = arrayIndex,
//                        minItems = minItems,
//                        maxItems = maxItems,
//                        uniqueItems = uniqueItems
//                    )
//                )
//                continue
//            }
//
//            val minimum = parameter.value.minimum?.toInt()
//            val maximum = parameter.value.maximum?.toInt()
//            val exclusiveMinimum = parameter.value.exclusiveMinimum ?: false
//            val exclusiveMaximum = parameter.value.exclusiveMaximum ?: false
//            val multipleOf = parameter.value.multipleOf?.toInt()
//            val pattern = parameter.value.pattern
//            val minLength = parameter.value.minLength
//            val maxLength = parameter.value.maxLength
//
//            properties.add(
//                Parameter(
//                    name = name,
//                    dataType = dataType,
//                    required = required,
//                    schemaName = schemaName,
//                    isEnum = values.isNotEmpty(),
//                    values = values,
//                    minimum = minimum,
//                    maximum = maximum,
//                    exclusiveMinimum = exclusiveMinimum,
//                    exclusiveMaximum = exclusiveMaximum,
//                    multipleOf = multipleOf,
//                    minLength = minLength,
//                    maxLength = maxLength,
//                    pattern = pattern
//                )
//            )
        }
    }

    return properties
}

fun getParameterProperties(parameter: Schema<*>, dataType: DataType): ParameterProperties? {
    when (dataType){
        DataType.STRING -> {
            return StringProperties(
                isEnum = parameter.enum != null,
                values = parameter.enum?.map { it.toString() } ?: emptyList(),
                minLength = parameter.minLength,
                maxLength = parameter.maxLength,
                pattern = parameter.pattern
            )
        }
        DataType.INTEGER, DataType.LONG, DataType.FLOAT, DataType.DOUBLE -> {
            return NumberProperties(
                minimum = parameter.minimum?.toInt(),
                maximum = parameter.maximum?.toInt(),
                exclusiveMinimum = parameter.exclusiveMinimum ?: false,
                exclusiveMaximum = parameter.exclusiveMaximum ?: false,
                multipleOf = parameter.multipleOf?.toInt()
            )
        }
        DataType.ARRAY -> {
            val arrayItemsDataType = DataType.fromString(parameter.items.type ?: "", parameter.items.format ?: "")
            return ArrayProperties(
                arrayItemsDataType = arrayItemsDataType,
                arrayItemsSchemaName = parameter.items.`$ref` ?: "",
                minItems = parameter.minItems,
                maxItems = parameter.maxItems,
                uniqueItems = parameter.uniqueItems ?: false,
                arrayProperties = getParameterProperties(parameter.items, arrayItemsDataType)
            )
        }
        else -> return null
    }
}

//private fun getArrayProperties(
//    arrayItems: Schema<*>?,
//    name: String,
//    dataType: DataType,
//    required: Boolean,
//    schemaName: String,
//    values: List<String>,
//    arrayIndex: Int,
//    minItems: Int?,
//    maxItems: Int?,
//    uniqueItems: Boolean = false
//): Parameter {
//    val childArrayItems = arrayItems?.items
//    val arrayItemsType = DataType.fromString(arrayItems?.items?.type ?: "", arrayItems?.items?.format ?: "")
//    val arrayItemsSchemaName = arrayItems?.items?.`$ref` ?: ""
//    val childUniqueItems = arrayItems?.uniqueItems ?: false
//    val childMinItems = arrayItems?.minItems
//    val childMaxItems = arrayItems?.maxItems
//    val childArrayIndex = arrayIndex + 1
//
//    if (arrayItemsType != DataType.ARRAY) {
//        return Parameter(
//            name = name,
//            dataType = dataType,
//            required = required,
//            schemaName = schemaName,
//            isEnum = values.isNotEmpty(),
//            arrayItemsType = arrayItemsType,
//            arrayItemsSchemaName = arrayItemsSchemaName,
//            values = values,
//            minItems = minItems,
//            maxItems = maxItems,
//            arrayIndex = arrayIndex,
//            uniqueItems = uniqueItems
//        )
//    }
//
//    return Parameter(
//        name = name,
//        dataType = dataType,
//        required = required,
//        schemaName = schemaName,
//        isEnum = values.isNotEmpty(),
//        arrayItemsType = arrayItemsType,
//        arrayItemsSchemaName = arrayItemsSchemaName,
//        values = values,
//        minItems = minItems,
//        maxItems = maxItems,
//        arrayIndex = arrayIndex,
//        uniqueItems = uniqueItems,
//        arrayProperties = getArrayProperties(
//            arrayItems = childArrayItems,
//            name = name,
//            dataType = dataType,
//            required = required,
//            schemaName = schemaName,
//            values = values,
//            minItems = childMinItems,
//            maxItems = childMaxItems,
//            arrayIndex = childArrayIndex,
//            uniqueItems = childUniqueItems
//        )
//    )
//}

//private fun removeDuplicates(components: MutableList<datamodel.Schema>): MutableList<datamodel.Schema> {
//    val sealedComponents = components.filter { it.superClassChildSchemaNames.isNotEmpty() }
//
//    for (component in sealedComponents) {
//        val oneOfSchemaNames = component.superClassChildSchemaNames
//        for (schemaName in oneOfSchemaNames) {
//            val relatedComponent = components.find { it.schemaName == schemaName } ?: continue
//            component.superClassChildSchemas.add(relatedComponent)
//            components.remove(relatedComponent)
//        }
//    }
//
//    return components
//}

//private fun checkChildren(components: MutableList<datamodel.Schema>): List<datamodel.Schema> {
//    val simplifiedNames = mutableListOf<Pair<String, String>>()
//
//    for (component in components) {
//        simplifiedNames.add(Pair(component.simplifiedName, component.simplifiedName.split("_").first()))
//    }
//
//    for ((key, value) in simplifiedNames) {
//        var parent = components.find { it.simplifiedName == value }
//        val child = components.find { it.simplifiedName == key }!!
//        if (parent != null && key != value && parent.parameters == child.parameters) {
//            parent.schemaNameChildren.add(child.schemaName)
//            parent.schemaNameChildren.addAll(child.schemaNameChildren)
//            components.remove(child)
//            continue
//        }
//
//        parent = components.find { it.parameters == child.parameters && it != child }
//        if (parent != null && ((key.contains("Body") && key.contains(parent.simplifiedName)) || key.contains("Response"))) {
//            parent.schemaNameChildren.add(child.schemaName)
//            parent.schemaNameChildren.addAll(child.schemaNameChildren)
//            components.remove(child)
//        }
//    }
//
//    return components
//}
