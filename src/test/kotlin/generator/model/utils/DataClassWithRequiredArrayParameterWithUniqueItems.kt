package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredArrayParameterWithUniqueItems = Component(
    schemaName = "#/components/schemas/DataClassWithRequiredArrayParameterWithUniqueItems",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.INTEGER,
            required = true,
            schemaName = "",
            uniqueItems = true
        )
    ),
    simplifiedName = "DataClassWithRequiredArrayParameterWithUniqueItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredArrayParameterWithUniqueItemsComponents = listOf(dataClassWithRequiredArrayParameterWithUniqueItems)