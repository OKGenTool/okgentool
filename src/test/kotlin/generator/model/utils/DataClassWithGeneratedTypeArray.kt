package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithGeneratedTypeArray = Component(
    schemaName = "#/components/schemas/DataClassWithGeneratedTypeArray",
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
            arrayItemsType = DataType.OBJECT,
            arrayItemsSchemaName = "#/components/schemas/DataClass"
        )
    ),
    simplifiedName = "DataClassWithGeneratedTypeArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithGeneratedTypeArrayComponents = listOf(dataClassWithGeneratedTypeArray, dataClass)