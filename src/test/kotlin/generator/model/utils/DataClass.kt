package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClass = Component(
    schemaName = "#/components/schemas/DataClass",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperties(
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