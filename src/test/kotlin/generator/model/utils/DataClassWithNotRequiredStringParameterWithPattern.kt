package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithNotRequiredStringParameterWithPattern = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredStringParameterWithPattern",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.STRING,
            required = false,
            schemaName = "",
            pattern = "^\\d{3}-\\d{2}-\\d{4}$"
        )
    ),
    simplifiedName = "DataClassWithNotRequiredStringParameterWithPattern",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredStringParameterWithPatternComponents = listOf(dataClassWithNotRequiredStringParameterWithPattern)