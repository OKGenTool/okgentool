package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithNotRequiredIntParameterWithMaxAndMin = Schema(
    schemaName = "#/components/schemas/dataClassWithNotRequiredIntParameterWithMaxAndMin",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            properties = NumberProperties(
                maximum = 10,
                minimum = 1
            )
        )
    ),
    simplifiedName = "dataClassWithNotRequiredIntParameterWithMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithMaxAndMinComponents = listOf(dataClassWithNotRequiredIntParameterWithMaxAndMin)