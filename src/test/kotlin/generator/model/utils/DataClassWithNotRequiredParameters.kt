package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithNotRequiredParameters = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredParameters",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = false,
            schemaName = ""
        ),
        ComponentProperties(
            name = "age",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = ""
        )
    ),
    simplifiedName = "DataClassWithNotRequiredParameters",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredParametersComponents = listOf(dataClassWithNotRequiredParameters)