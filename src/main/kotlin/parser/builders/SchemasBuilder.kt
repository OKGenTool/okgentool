package parser.builders

import datamodel.*
import generator.capitalize
import io.swagger.v3.oas.models.media.Schema
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("SchemasBuilder.kt")

fun buildSchemas(inlineSchemas: MutableList<InlineSchema>): List<datamodel.Schema> {
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

    for (inlineSchema in inlineSchemas) {
        val requiredProperties = inlineSchema.schema.required ?: emptyList()
        val parameters = getParameters(inlineSchema.schema, requiredProperties)
        val oneOfSchemaNames = getOneOfSchemaNames(inlineSchema.schema)
        val schemaName = inlineSchema.name
        logger.info("Parsing schema: $schemaName")
        res.add(datamodel.Schema(schemaName, parameters, inlineSchema.name.capitalize(), oneOfSchemaNames))
    }

    return res
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
            val arrayProperties = if (arrayItemsDataType == DataType.ARRAY) {
                getParameterProperties(parameter.items, arrayItemsDataType)
            } else {
                null
            }
            return ArrayProperties(
                arrayItemsDataType = arrayItemsDataType,
                arrayItemsSchemaName = parameter.items.`$ref` ?: "",
                minItems = parameter.minItems,
                maxItems = parameter.maxItems,
                uniqueItems = parameter.uniqueItems ?: false,
                arrayProperties = arrayProperties
            )
        }
        else -> return null
    }
}