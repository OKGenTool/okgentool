package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithRequiredIntParameterWithExclusiveMaxAndMin = Schema(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithExclusiveMaxAndMin",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            properties = NumberProperties(
                maximum = 10,
                minimum = 1,
                exclusiveMaximum = true,
                exclusiveMinimum = true
            )
        )
    ),
    simplifiedName = "dataClassWithRequiredIntParameterWithExclusiveMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithExclusiveMaxAndMinComponents = listOf(dataClassWithRequiredIntParameterWithExclusiveMaxAndMin)