package generator.model.utils

import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

val sealedClass = Schema(
    schemaName = "#/components/schemas/SealedClass",
    parameters = emptyList(),
    simplifiedName = "SealedClass",
    superClassChildSchemaNames = listOf(
        "#/components/schemas/Child1",
        "#/components/schemas/Child2"
    ),
    superClassChildSchemas = mutableListOf(
        Schema(
            schemaName = "#/components/schemas/Child1",
            parameters = listOf(
                Parameter(
                    name = "name",
                    dataType = DataType.STRING,
                    required = true,
                    schemaName = ""
                )
            ),
            simplifiedName = "Child1",
            superClassChildSchemaNames = emptyList()
        ),
        Schema(
            schemaName = "#/components/schemas/Child2",
            parameters = listOf(
                Parameter(
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