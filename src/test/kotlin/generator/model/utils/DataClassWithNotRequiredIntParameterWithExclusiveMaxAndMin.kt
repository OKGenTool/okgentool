package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin = Schema(
    schemaName = "#/components/schemas/dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            properties = NumberProperties(
                maximum = 10,
                minimum = 1,
                exclusiveMaximum = true,
                exclusiveMinimum = true

            ),
        )
    ),
    simplifiedName = "dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMinComponents = listOf(dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin)