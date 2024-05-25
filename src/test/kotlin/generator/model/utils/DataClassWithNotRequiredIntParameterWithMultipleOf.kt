package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithNotRequiredIntParameterWithMultipleOf = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.INTEGER,
            required = false,
            schemaName = "",
            multipleOf = 2
        )
    ),
    simplifiedName = "DataClassWithNotRequiredIntParameterWithMultipleOf",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredIntParameterWithMultipleOfComponents = listOf(dataClassWithNotRequiredIntParameterWithMultipleOf)