package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithNotRequiredIntParameterWithMaxAndMin = Component(
    schemaName = "#/components/schemas/dataClassWithNotRequiredIntParameterWithMaxAndMin",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            maximum = 10,
            minimum = 1
        )
    ),
    simplifiedName = "dataClassWithNotRequiredIntParameterWithMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithMaxAndMinComponents = listOf(dataClassWithNotRequiredIntParameterWithMaxAndMin)