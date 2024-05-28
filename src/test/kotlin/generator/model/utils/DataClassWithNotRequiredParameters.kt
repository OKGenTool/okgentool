package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithNotRequiredParameters = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredParameters",
    parameters = listOf(
        ComponentProperty(
            name = "name",
            dataType = DataType.STRING,
            required = false,
            schemaName = ""
        ),
        ComponentProperty(
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