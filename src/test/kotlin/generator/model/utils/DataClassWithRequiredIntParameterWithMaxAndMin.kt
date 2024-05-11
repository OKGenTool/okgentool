package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithRequiredIntParameterWithMaxAndMin = Component(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithMaxAndMin",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            maximum = 10,
            minimum = 1
        )
    ),
    simplifiedName = "dataClassWithRequiredIntParameterWithMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithMaxAndMinComponents = listOf(dataClassWithRequiredIntParameterWithMaxAndMin)