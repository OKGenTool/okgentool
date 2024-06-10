package datamodel

data class Schema(
    val schemaName: String,
    val parameters: List<Parameter>,
    val simplifiedName: String,
    val superClassChildSchemaNames: List<String>,
    val superClassChildSchemas: MutableList<Schema> = mutableListOf(),
    val schemaNameChildren: MutableList<String> = mutableListOf(),
)
