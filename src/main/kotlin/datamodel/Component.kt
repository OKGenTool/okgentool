package datamodel

data class Component(
    val schemaName: String,
    val parameters: List<ComponentProperties>,
    val simplifiedName: String,
    val schemaNameSons: MutableList<String> = mutableListOf()
)
