package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithNotRequiredIntParameterWithMultipleOf = Component(
    schemaName = "#/components/schemas/dataClassWithNotRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        ComponentProperties(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            multipleOf = 2
        )
    ),
    simplifiedName = "dataClassWithNotRequiredIntParameterWithMultipleOf",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithMultipleOfComponents = listOf(dataClassWithNotRequiredIntParameterWithMultipleOf)