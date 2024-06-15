package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithArray = Schema(
    schemaName = "#/components/schemas/DataClassWithArray",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        Parameter(
            name = "values",
            dataType = DataType.ARRAY,
            properties = ArrayProperties(
                arrayItemsDataType = DataType.INTEGER
            ),
            required = true,
            schemaName = ""
        )
    ),
    simplifiedName = "DataClassWithArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithArrayComponents = listOf(dataClassWithArray)