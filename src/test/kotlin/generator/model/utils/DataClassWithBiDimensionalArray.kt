package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithBiDimensionalArray = Schema(
    schemaName = "#/components/schemas/DataClassWithBiDimensionalArray",
    parameters = listOf(
        Parameter(
            name = "values",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.ARRAY,
                arrayProperties = ArrayProperties(
                    arrayItemsDataType = DataType.INTEGER
                )
            ),
            required = true,
            schemaName = "",
        )
    ),
    simplifiedName = "DataClassWithBiDimensionalArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithBiDimensionalArrayComponents = listOf(dataClassWithBiDimensionalArray)