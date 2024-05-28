package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredStringParameterWithPattern = Component(
    schemaName = "#/components/schemas/DataClassWithRequiredStringParameterWithPattern",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            pattern = "^\\d{3}-\\d{2}-\\d{4}$"
        )
    ),
    simplifiedName = "DataClassWithRequiredStringParameterWithPattern",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredStringParameterWithPatternComponents = listOf(dataClassWithRequiredStringParameterWithPattern)