package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithArray = Component(
    schemaName = "#/components/schemas/DataClassWithArray",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperties(
            name = "values",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.INTEGER,
            required = true,
            schemaName = ""
        )
    ),
    simplifiedName = "DataClassWithArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithArrayComponents = listOf(dataClassWithArray)