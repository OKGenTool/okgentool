package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.StringProperties

val dataClassWithNotRequiredStringParameterWithPattern = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredStringParameterWithPattern",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.STRING,
            required = false,
            schemaName = "",
            properties = StringProperties(
                pattern = "^\\d{3}-\\d{2}-\\d{4}$"
            ),
        )
    ),
    simplifiedName = "DataClassWithNotRequiredStringParameterWithPattern",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredStringParameterWithPatternComponents = listOf(dataClassWithNotRequiredStringParameterWithPattern)