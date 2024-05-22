package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithNotRequiredArrayParameterWithMinMaxItems = Component(
    schemaName = "#/components/schemas/DataClassWithNotRequiredArrayParameterWithMinMaxItems",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.INTEGER,
            required = false,
            schemaName = "",
            minItems = 2,
            maxItems = 10
        )
    ),
    simplifiedName = "DataClassWithNotRequiredArrayParameterWithMinMaxItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredArrayParameterWithMinMaxItemsComponents = listOf(dataClassWithNotRequiredArrayParameterWithMinMaxItems)