package generator.model.utils

import datamodel.ArrayProperties
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithGeneratedTypeArray = Schema(
    schemaName = "#/components/schemas/DataClassWithGeneratedTypeArray",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        Parameter(
            name = "value",
            dataType = DataType.ARRAY,
            required = true,
            schemaName = "",
            properties = ArrayProperties(
                arrayItemsDataType = DataType.OBJECT,
                arrayItemsSchemaName = "#/components/schemas/DataClass"
            )
        )
    ),
    simplifiedName = "DataClassWithGeneratedTypeArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithGeneratedTypeArrayComponents = listOf(dataClassWithGeneratedTypeArray, dataClass)