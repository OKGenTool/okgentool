package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithRequiredArrayParameterWithMinMaxItems = Schema(
    schemaName = "#/components/schemas/DataClassWithRequiredArrayParameterWithMinMaxItems",
    parameters = listOf(
        Parameter(
            name = "value",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.INTEGER,
                minItems = 2,
                maxItems = 10
            ),
            required = true,
            schemaName = "",
        )
    ),
    simplifiedName = "DataClassWithRequiredArrayParameterWithMinMaxItems",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithRequiredArrayParameterWithMinMaxItemsComponents = listOf(dataClassWithRequiredArrayParameterWithMinMaxItems)