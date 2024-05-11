package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithRequiredIntParameterWithMultipleOf = Component(
    schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithMultipleOf",
    parameters = listOf(
        ComponentProperties(
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