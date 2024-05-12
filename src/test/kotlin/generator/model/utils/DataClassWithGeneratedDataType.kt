package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithGeneratedDataType = Component(
    schemaName = "#/components/schemas/DataClassWithGeneratedDataType",
    parameters = listOf(
        ComponentProperty(
            name = "name",
            dataType = DataType.STRING,
            required = true,
            schemaName = ""
        ),
        ComponentProperty(
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