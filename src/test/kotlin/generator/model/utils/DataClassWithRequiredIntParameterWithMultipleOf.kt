package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredIntParameterWithMultipleOf = Component(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.INTEGER,
            required = true,
            schemaName = "",
            multipleOf = 2
        )
    ),
    simplifiedName = "dataClassWithRequiredIntParameterWithMultipleOf",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredIntParameterWithMultipleOfComponents = listOf(dataClassWithRequiredIntParameterWithMultipleOf)