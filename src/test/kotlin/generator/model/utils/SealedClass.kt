package generator.model.utils

import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType

val sealedClass = Component(
    schemaName = "#/components/schemas/SealedClass",
    parameters = emptyList(),
    simplifiedName = "SealedClass",
    superClassChildSchemaNames = listOf(
        "#/components/schemas/Child1",
        "#/components/schemas/Child2"
    ),
    superClassChildComponents = mutableListOf(
        Component(
            schemaName = "#/components/schemas/Child1",
            parameters = listOf(
                ComponentProperty(
                    name = "name",
                    dataType = DataType.STRING,
                    required = true,
                    schemaName = ""
                )
            ),
            simplifiedName = "Child1",
            superClassChildSchemaNames = emptyList()
        ),
        Component(
            schemaName = "#/components/schemas/Child2",
            parameters = listOf(
                ComponentProperty(
                    name = "age",
                    dataType = DataType.INTEGER,
                    required = true,
                    schemaName = ""
                )
            ),
            simplifiedName = "Child2",
            superClassChildSchemaNames = emptyList()
        )
    )
)

val sealedClassComponents = listOf(sealedClass)