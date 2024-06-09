package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val dataClassWithGeneratedDataType = Schema(
    schemaName = "#/components/schemas/DataClassWithGeneratedDataType",
    parameters = listOf(
        Parameter(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        Parameter(
            name = "value",
            dataType = DataType.OBJECT,
            required = true,
            schemaName = "#/components/schemas/DataClass"
        )
    ),
    simplifiedName = "DataClassWithGeneratedDataType",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithGeneratedDataTypeComponents = listOf(dataClassWithGeneratedDataType, dataClass)