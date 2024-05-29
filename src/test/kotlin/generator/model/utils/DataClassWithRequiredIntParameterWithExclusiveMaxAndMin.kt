package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredIntParameterWithExclusiveMaxAndMin = Component(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithExclusiveMaxAndMin",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            maximum = 10,
            minimum = 1,
            exclusiveMaximum = true,
            exclusiveMinimum = true
        )
    ),
    simplifiedName = "dataClassWithRequiredIntParameterWithExclusiveMaxAndMin",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithExclusiveMaxAndMinComponents = listOf(dataClassWithRequiredIntParameterWithExclusiveMaxAndMin)