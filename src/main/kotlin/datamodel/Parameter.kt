package datamodel

data class Parameter(
    val name: String,
    val dataType: DataType,
    val required: Boolean,
    val schemaName: String,
    val properties: ParameterProperties? = null,
    val example: Any? = null,
)
