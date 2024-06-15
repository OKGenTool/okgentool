package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.StringProperties

val dataClassWithNotRequiredStringParameterWithMinMaxLen = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredStringParameterWithMinMaxLen",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.STRING,
            required = false,
            schemaName = "",
            properties = StringProperties(
                minLength = 2,
                maxLength = 10
            ),
        )
    ),
    simplifiedName = "DataClassWithNotRequiredStringParameterWithMinMaxLen",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredStringParameterWithMinMaxLenComponents = listOf(dataClassWithNotRequiredStringParameterWithMinMaxLen)