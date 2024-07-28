package datamodel

data class Schema(
    val schemaName: String,
    val parameters: List<Parameter>,
    val simplifiedName: String,
    val superClassChildSchemaNames: List<String>,
    val superClassChildSchemas: MutableList<Schema> = mutableListOf(),
)

fun Schema.hasObjectParameter(): Boolean {
    return this.parameters.any { parameter ->
        parameter.dataType == DataType.OBJECT ||
                (parameter.dataType == DataType.ARRAY &&
                        (parameter.properties as? ArrayProperties)?.arrayItemsDataType == DataType.OBJECT)
    }
}

fun Schema.isDependentOn(other: Schema): Boolean {
    return this.parameters.any { parameter ->
        parameter.schemaName == other.schemaName ||
                (parameter.dataType == DataType.ARRAY &&
                        (parameter.properties as? ArrayProperties)?.arrayItemsSchemaName == other.schemaName)
    }
}

fun compareSchemas(schema1: Schema, schema2: Schema): Int {
    val schema1HasObject = schema1.hasObjectParameter()
    val schema2HasObject = schema2.hasObjectParameter()

    return when {
        schema1HasObject && schema2HasObject -> {
            when {
                schema1.isDependentOn(schema2) -> 1
                schema2.isDependentOn(schema1) -> -1
                else -> 0
            }
        }
        schema1HasObject -> 1
        schema2HasObject -> -1
        else -> 0
    }
}
