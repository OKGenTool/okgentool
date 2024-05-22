package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithNotRequiredArrayParameterWithUniqueItems = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredArrayParameterWithUniqueItems",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.INTEGER,
            required = false,
            schemaName = "",
            uniqueItems = true
        )
    ),
    simplifiedName = "DataClassWithNotRequiredArrayParameterWithUniqueItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredArrayParameterWithUniqueItemsComponents = listOf(dataClassWithNotRequiredArrayParameterWithUniqueItems)