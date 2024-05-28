package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val dataClassWithBiDimensionalArray = Component(
    schemaName = "#/components/schemas/DataClassWithBiDimensionalArray",
    parameters = listOf(
        ComponentProperty(
            name = "values",
            dataType = DataType.ARRAY,
            arrayItemsType = DataType.ARRAY,
            required = true,
            schemaName = "",
            arrayProperties = ComponentProperty(
                name = "values",
                dataType = DataType.ARRAY,
                arrayItemsType = DataType.INTEGER,
                required = true,
                schemaName = ""
            )
        )
    ),
    simplifiedName = "DataClassWithBiDimensionalArray",
    superClassChildSchemaNames = emptyList()
)

val dataClassWithBiDimensionalArrayComponents = listOf(dataClassWithBiDimensionalArray)