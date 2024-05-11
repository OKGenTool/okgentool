package generator.model

import com.google.common.io.Resources
import generator.builders.model.createModelComponent
import generator.model.utils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelBuilderGenerationTests {

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

    @Test
    fun `Generate data class with required int parameter with maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithMaxAndMin,
            dataClassWithRequiredIntParameterWithMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithMaxAndMin")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithMaxAndMin,
            dataClassWithNotRequiredIntParameterWithMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithMaxAndMin")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required int parameter with exclusive maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithExclusiveMaxAndMin,
            dataClassWithRequiredIntParameterWithExclusiveMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithExclusiveMaxAndMin")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with exclusive maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin,
            dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with multipleOf`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithMultipleOf,
            dataClassWithNotRequiredIntParameterWithMultipleOfComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithMultipleOf")

        assertEquals(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required int parameter with multipleOf`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithMultipleOf,
            dataClassWithRequiredIntParameterWithMultipleOfComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithMultipleOf")

        assertEquals(referenceContent, fileSpec)
    }

    private fun getReferenceContent(fileName: String): String {
        return Resources.getResource("generator/model/$fileName").readText().replace("\r\n", "\n")
    }
}
