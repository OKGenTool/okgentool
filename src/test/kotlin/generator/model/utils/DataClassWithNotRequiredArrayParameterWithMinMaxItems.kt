package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithNotRequiredArrayParameterWithMinMaxItems = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredArrayParameterWithMinMaxItems",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.INTEGER,
                minItems = 2,
                maxItems = 10
            ),
            required = false,
            schemaName = "",
        )
    ),
    simplifiedName = "DataClassWithNotRequiredArrayParameterWithMinMaxItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredArrayParameterWithMinMaxItemsComponents = listOf(dataClassWithNotRequiredArrayParameterWithMinMaxItems)