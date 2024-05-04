package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithGeneratedTypeArray = Component(
    schemaName = "#/components/schemas/DataClassWithGeneratedTypeArray",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperties(
            name = "value",
            dataType = DataType.ARRAY,
            required = true,
            schemaName = "",
            arrayItemsSchemaName = "#/components/schemas/DataClass"
        )
    ),
    simplifiedName = "DataClassWithGeneratedTypeArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithGeneratedTypeArrayComponents = listOf(dataClassWithGeneratedTypeArray, dataClass)