package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

val dataClassWithGeneratedDataType = Component(
    schemaName = "#/components/schemas/DataClassWithGeneratedDataType",
    parameters = listOf(
        ComponentProperties(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperties(
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