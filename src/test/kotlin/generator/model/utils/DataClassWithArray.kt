package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithArray = Component(
    schemaName = "#/components/schemas/DataClassWithArray",
    parameters = listOf(
        ComponentProperty(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperty(
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