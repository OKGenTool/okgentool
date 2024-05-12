package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithNotRequiredStringParameterWithMinMaxLen = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredStringParameterWithMinMaxLen",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.STRING,
            required = false,
            schemaName = "",
            minLength = 2,
            maxLength = 10
        )
    ),
    simplifiedName = "DataClassWithNotRequiredStringParameterWithMinMaxLen",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredStringParameterWithMinMaxLenComponents = listOf(dataClassWithNotRequiredStringParameterWithMinMaxLen)