package datamodel

sealed class ParameterProperties

data class ArrayProperties(
    val arrayItemsDataType: DataType? = null,
    val arrayItemsSchemaName: String? = null,
    val minItems: Int? = null,
    val maxItems: Int? = null,
    val uniqueItems: Boolean = false,
    val arrayProperties: ParameterProperties? = null,
) : ParameterProperties()

data class NumberProperties(
    val minimum: Int? = null,
    val maximum: Int? = null,
    val exclusiveMinimum: Boolean = false,
    val exclusiveMaximum: Boolean = false,
    val multipleOf: Int? = null
) : ParameterProperties()

data class StringProperties(
    val isEnum: Boolean = false,
    val values: List<String> = emptyList(),
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val pattern: String? = null
) : ParameterProperties()