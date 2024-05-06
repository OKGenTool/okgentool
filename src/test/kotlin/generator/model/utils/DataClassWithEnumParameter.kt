package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithEnumParameter = Component(
    schemaName = "#/components/schemas/DataClassWithEnumParameter",
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
            arrayItemsSchemaName = "",
            isEnum = true,
            values = listOf("A", "B", "C")
        )
    ),
    simplifiedName = "DataClassWithGeneratedTypeArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithEnumParameterComponents = listOf(dataClassWithEnumParameter)