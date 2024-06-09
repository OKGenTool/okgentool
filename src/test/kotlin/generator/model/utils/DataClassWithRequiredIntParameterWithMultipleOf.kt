package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithRequiredIntParameterWithMultipleOf = Schema(
    schemaName = "#/components/schemas/DataClassWithRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            properties = NumberProperties(
                multipleOf = 2
            ),
        )
    ),
    simplifiedName = "DataClassWithRequiredIntParameterWithMultipleOf",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithMultipleOfComponents = listOf(dataClassWithRequiredIntParameterWithMultipleOf)