package parser.model

data class ComponentProperties(
    val name: String,
    val dataType: DataType?,
    val required: Boolean,
    val schemaName: String
)
