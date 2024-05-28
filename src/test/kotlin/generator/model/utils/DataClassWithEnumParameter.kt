package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithEnumParameter = Component(
    schemaName = "#/components/schemas/DataClassWithEnumParameter",
    parameters = listOf(
        ComponentProperty(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperty(
            name = "value",
            dataType = DataType.ARRAY,
            required = true,
            schemaName = "",
            arrayItemsSchemaName = "",
            isEnum = true,
            values = listOf("A", "B", "C")
        )
    ),
    simplifiedName = "DataClassWithEnumParameter",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithEnumParameterComponents = listOf(dataClassWithEnumParameter)