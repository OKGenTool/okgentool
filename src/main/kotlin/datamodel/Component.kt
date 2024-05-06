package datamodel

data class Component(
    val schemaName: String,
    val parameters: List<ComponentProperties>,
    val simplifiedName: String,
    val superClassChildSchemaNames: List<String>,
    val superClassChildComponents: MutableList<Component> = mutableListOf(),
    val schemaNameChildren: MutableList<String> = mutableListOf(),
)
