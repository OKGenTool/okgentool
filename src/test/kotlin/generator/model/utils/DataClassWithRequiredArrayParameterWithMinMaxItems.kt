package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithRequiredArrayParameterWithMinMaxItems = Component(
    schemaName = "#/components/schemas/DataClassWithRequiredArrayParameterWithMinMaxItems",
    parameters = listOf(
        ComponentProperty(
            name = "value",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.INTEGER,
            required = true,
            schemaName = "",
            minItems = 2,
            maxItems = 10
        )
    ),
    simplifiedName = "DataClassWithRequiredArrayParameterWithMinMaxItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredArrayParameterWithMinMaxItemsComponents = listOf(dataClassWithRequiredArrayParameterWithMinMaxItems)