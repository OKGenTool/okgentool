package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClass = Schema(
    schemaName = "#/components/schemas/DataClass",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        Parameter(
            name = "age",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = ""
        )
    ),
    simplifiedName = "DataClass",
    superClassChildSchemaNames = emptyList()
)

val dataClassComponents = listOf(dataClass)