package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.NumberProperties

val dataClassWithRequiredIntParameterWithMaxAndMin = Schema(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithMaxAndMin",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            properties = NumberProperties(
                maximum = 10,
                minimum = 1
            ),
        )
    ),
    simplifiedName = "dataClassWithRequiredIntParameterWithMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithMaxAndMinComponents = listOf(dataClassWithRequiredIntParameterWithMaxAndMin)