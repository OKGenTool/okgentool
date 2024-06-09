package datamodel

data class Parameter(
    val name: String,
    val dataType: DataType,
    val required: Boolean,
    val schemaName: String,
    val properties: ParameterProperties? = null,
    val example: Any? = null,


//    val isEnum: Boolean = false,
//    val arrayItemsType: DataType? = null,
//    val arrayItemsSchemaName: String? = null,
//    val values: List<String> = emptyList(),
//    val minimum: Int? = null,
//    val maximum: Int? = null,
//    val exclusiveMinimum: Boolean = false,
//    val exclusiveMaximum: Boolean = false,
//    val multipleOf: Int? = null,
//    val minLength: Int? = null,
//    val maxLength: Int? = null,
//    val pattern: String? = null,
//    val minItems: Int? = null,
//    val maxItems: Int? = null,
//    val uniqueItems: Boolean = false,
//    val arrayProperties: Parameter? = null,
//    val arrayIndex: Int? = null
)
