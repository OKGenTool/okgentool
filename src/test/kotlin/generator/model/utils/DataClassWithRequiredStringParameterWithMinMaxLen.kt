package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithRequiredStringParameterWithMinMaxLen = Component(
    schemaName = "#/components/schemas/DataClassWithRequiredStringParameterWithMinMaxLen",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            minLength = 2,
            maxLength = 10
        )
    ),
    simplifiedName = "DataClassWithRequiredStringParameterWithMinMaxLen",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredStringParameterWithMinMaxLenComponents = listOf(dataClassWithRequiredStringParameterWithMinMaxLen)