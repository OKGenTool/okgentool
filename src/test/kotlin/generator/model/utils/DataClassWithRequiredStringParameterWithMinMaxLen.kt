package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.StringProperties

val dataClassWithRequiredStringParameterWithMinMaxLen = Schema(
    schemaName = "#/components/schemas/DataClassWithRequiredStringParameterWithMinMaxLen",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            properties = StringProperties(
                minLength = 2,
                maxLength = 10
            ),
        )
    ),
    simplifiedName = "DataClassWithRequiredStringParameterWithMinMaxLen",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredStringParameterWithMinMaxLenComponents = listOf(dataClassWithRequiredStringParameterWithMinMaxLen)