package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClass = Component(
    schemaName = "#/components/schemas/DataClass",
    parameters = listOf(
        ComponentProperty(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperty(
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