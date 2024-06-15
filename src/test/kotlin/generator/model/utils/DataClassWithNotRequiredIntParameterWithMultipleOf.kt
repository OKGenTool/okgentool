package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithNotRequiredIntParameterWithMultipleOf = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            properties = NumberProperties(
                multipleOf = 2
            ),
        )
    ),
    simplifiedName = "DataClassWithNotRequiredIntParameterWithMultipleOf",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithMultipleOfComponents = listOf(dataClassWithNotRequiredIntParameterWithMultipleOf)