package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin = Component(
    schemaName = "#/components/schemas/dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            maximum = 10,
            minimum = 1,
            exclusiveMaximum = true,
            exclusiveMinimum = true
        )
    ),
    simplifiedName = "dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMinComponents = listOf(dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin)