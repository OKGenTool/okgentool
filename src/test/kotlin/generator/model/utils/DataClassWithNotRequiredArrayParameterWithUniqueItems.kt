package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithNotRequiredArrayParameterWithUniqueItems = Schema(
    schemaName = "#/components/schemas/DataClassWithNotRequiredArrayParameterWithUniqueItems",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.INTEGER,
                uniqueItems = true
            ),
            required = false,
            schemaName = "",
        )
    ),
    simplifiedName = "DataClassWithNotRequiredArrayParameterWithUniqueItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithNotRequiredArrayParameterWithUniqueItemsComponents = listOf(dataClassWithNotRequiredArrayParameterWithUniqueItems)