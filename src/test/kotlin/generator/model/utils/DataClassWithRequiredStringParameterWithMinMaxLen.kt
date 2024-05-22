package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredStringParameterWithMinMaxLen = Component(
    schemaName = "#/components/schemas/DataClassWithRequiredStringParameterWithMinMaxLen",
    parameters = listOf(
        ComponentProperty(
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