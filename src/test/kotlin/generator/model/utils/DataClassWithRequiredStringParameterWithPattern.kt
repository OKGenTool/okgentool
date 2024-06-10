package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.StringProperties

val dataClassWithRequiredStringParameterWithPattern = Schema(
    schemaName = "#/components/schemas/DataClassWithRequiredStringParameterWithPattern",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            properties = StringProperties(
                pattern = "^\\d{3}-\\d{2}-\\d{4}$"
            ),
        )
    ),
    simplifiedName = "DataClassWithRequiredStringParameterWithPattern",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredStringParameterWithPatternComponents = listOf(dataClassWithRequiredStringParameterWithPattern)