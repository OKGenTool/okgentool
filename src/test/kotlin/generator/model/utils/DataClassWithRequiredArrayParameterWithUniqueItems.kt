package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithRequiredArrayParameterWithUniqueItems = Schema(
    schemaName = "#/components/schemas/DataClassWithRequiredArrayParameterWithUniqueItems",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.INTEGER,
                uniqueItems = true
            ),
            required = true,
            schemaName = "",
        )
    ),
    simplifiedName = "DataClassWithRequiredArrayParameterWithUniqueItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredArrayParameterWithUniqueItemsComponents = listOf(dataClassWithRequiredArrayParameterWithUniqueItems)