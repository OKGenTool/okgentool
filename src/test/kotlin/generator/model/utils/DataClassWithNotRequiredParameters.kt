package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithNotRequiredParameters = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredParameters",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = false,
            schemaName = ""
        ),
        Parameter(
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