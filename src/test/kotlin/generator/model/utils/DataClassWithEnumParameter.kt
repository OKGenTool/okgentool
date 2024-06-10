package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType
import datamodel.StringProperties

val dataClassWithEnumParameter = Schema(
    schemaName = "#/components/schemas/DataClassWithEnumParameter",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        Parameter(
            name = "value",
            dataType = DataType.STRING,
            required = true,
            schemaName = "",
            properties = StringProperties(
                isEnum = true,
                values = listOf("A", "B", "C")
            )
        )
    ),
    simplifiedName = "DataClassWithEnumParameter",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithEnumParameterComponents = listOf(dataClassWithEnumParameter)