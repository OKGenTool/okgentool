package generator.model

import com.google.common.io.Resources
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.builders.model.createModelComponent
import generator.model.utils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelBuilderTests {

    @Test
    fun `Generate data class`() {
        val fileSpec = createModelComponent(dataClass, dataClassComponents).toString()

        val referenceContent = getReferenceContent("DataClass")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required parameters`() {
        val fileSpec = createModelComponent(dataClassWithNotRequiredParameters, dataClassWithNotRequiredParametersComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredParameters")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate sealed class`() {
        val fileSpec = createModelComponent(sealedClass, sealedClassComponents).toString()

        val referenceContent = getReferenceContent("SealedClass")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with generated data type`() {
        val fileSpec = createModelComponent(dataClassWithGeneratedDataType, dataClassWithGeneratedDataTypeComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithGeneratedDataType")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with array`() {
        val fileSpec = createModelComponent(dataClassWithArray, dataClassWithArrayComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithArray")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with generated data type array`() {
        val fileSpec = createModelComponent(dataClassWithGeneratedTypeArray, dataClassWithGeneratedTypeArrayComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithGeneratedTypeArray")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with enum parameter`() {
        val fileSpec = createModelComponent(dataClassWithEnumParameter, dataClassWithEnumParameterComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithEnumParameter")

        assertEquals(referenceContent, fileSpec)
    }

    private fun getReferenceContent(fileName: String): String {
        return Resources.getResource("generator/model/$fileName").readText().replace("\r\n", "\n")
    }
}
