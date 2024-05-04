package generator.model

import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.builders.model.createModelComponent
import org.junit.jupiter.api.Test

class ModelBuilderTests {

    @Test
    fun `Generate simple data class`() {
        val component = Component(
            schemaName = "SimpleComponent",
            parameters = listOf(
                ComponentProperties(
                    name = "name",
                    dataType = DataType.STRING,
                    required = true,
                    schemaName = "name"
                ),
                ComponentProperties(
                    name = "age",
                    dataType = DataType.INTEGER,
                    required = true,
                    schemaName = "age"
                )
            ),
            simplifiedName = "SimpleComponent",
            superClassChildSchemaNames = emptyList()
        )

        val fileSpec = createModelComponent(component).toString()

        println(fileSpec)
    }
}
