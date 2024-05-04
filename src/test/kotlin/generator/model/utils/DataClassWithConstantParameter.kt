package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithConstantParameter = Component(
    schemaName = "#/components/schemas/DataClassWithConstantParameter",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperties(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            arrayItemsSchemaName = "",
            isEnum = true,
            values = listOf("ABC"),
        )
    ),
    simplifiedName = "DataClassWithConstantParameter",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithConstantParameterComponents = listOf(dataClassWithConstantParameter)