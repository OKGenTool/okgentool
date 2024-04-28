package datamodel

data class ComponentProperties(
    val name: String,
    val dataType: DataType?,
    val required: Boolean,
    val schemaName: String,
    val arrayItemsType: DataType? = null,
    val arrayItemsSchemaName: String? = null
)
