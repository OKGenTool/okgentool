package generator.model

import com.google.common.io.Resources
import generator.builders.model.createModelComponent
import generator.model.utils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class GenerationTests {

    @Test
    fun `Generate data class`() {
        val fileSpec = createModelComponent(dataClass, dataClassComponents).toString()

        val referenceContent = getReferenceContent("DataClass.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required parameters`() {
        val fileSpec = createModelComponent(dataClassWithNotRequiredParameters, dataClassWithNotRequiredParametersComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredParameters.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate sealed class`() {
        val fileSpec = createModelComponent(sealedClass, sealedClassComponents).toString()

        val referenceContent = getReferenceContent("SealedClass.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with generated data type`() {
        val fileSpec = createModelComponent(dataClassWithGeneratedDataType, dataClassWithGeneratedDataTypeComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithGeneratedDataType.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with array`() {
        val fileSpec = createModelComponent(dataClassWithArray, dataClassWithArrayComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithArray.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with bi-dimensional array`() {
        val fileSpec = createModelComponent(
            dataClassWithBiDimensionalArray,
            dataClassWithBiDimensionalArrayComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithBiDimensionalArray.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with generated data type array`() {
        val fileSpec = createModelComponent(dataClassWithGeneratedTypeArray, dataClassWithGeneratedTypeArrayComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithGeneratedTypeArray.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with enum parameter`() {
        val fileSpec = createModelComponent(dataClassWithEnumParameter, dataClassWithEnumParameterComponents).toString()

        val referenceContent = getReferenceContent("DataClassWithEnumParameter.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required int parameter with maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithMaxAndMin,
            dataClassWithRequiredIntParameterWithMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithMaxAndMin.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithMaxAndMin,
            dataClassWithNotRequiredIntParameterWithMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithMaxAndMin.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required int parameter with exclusive maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithExclusiveMaxAndMin,
            dataClassWithRequiredIntParameterWithExclusiveMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithExclusiveMaxAndMin.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with exclusive maximum and minimum`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin,
            dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMinComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required int parameter with multipleOf`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredIntParameterWithMultipleOf,
            dataClassWithNotRequiredIntParameterWithMultipleOfComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredIntParameterWithMultipleOf.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required int parameter with multipleOf`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredIntParameterWithMultipleOf,
            dataClassWithRequiredIntParameterWithMultipleOfComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredIntParameterWithMultipleOf.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required string parameter with minimum and maximum length`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredStringParameterWithMinMaxLen,
            dataClassWithNotRequiredStringParameterWithMinMaxLenComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredStringParameterWithMinMaxLen.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required string parameter with minimum and maximum length`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredStringParameterWithMinMaxLen,
            dataClassWithRequiredStringParameterWithMinMaxLenComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredStringParameterWithMinMaxLen.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

//    @Test
//    fun `Generate data class with not required string parameter with pattern`() {
//        val fileSpec = createModelComponent(
//            dataClassWithNotRequiredStringParameterWithPattern,
//            dataClassWithNotRequiredStringParameterWithPatternComponents
//        ).toString()
//
//        val referenceContent = getReferenceContent("DataClassWithNotRequiredStringParameterWithPattern.kt")
//
//        assertGeneratedMatch(referenceContent, fileSpec)
//    }
//
//    @Test
//    fun `Generate data class with required string parameter with pattern`() {
//        val fileSpec = createModelComponent(
//            dataClassWithRequiredStringParameterWithPattern,
//            dataClassWithRequiredStringParameterWithPatternComponents
//        ).toString()
//
//        val referenceContent = getReferenceContent("DataClassWithRequiredStringParameterWithPattern.kt")
//
//        assertGeneratedMatch(referenceContent, fileSpec)
//    }

    @Test
    fun `Generate data class with not required array parameter with minimum and maximum items`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredArrayParameterWithMinMaxItems,
            dataClassWithNotRequiredArrayParameterWithMinMaxItemsComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredArrayParameterWithMinMaxItems.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required array parameter with minimum and maximum items`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredArrayParameterWithMinMaxItems,
            dataClassWithRequiredArrayParameterWithMinMaxItemsComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredArrayParameterWithMinMaxItems.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with not required array parameter with unique items`() {
        val fileSpec = createModelComponent(
            dataClassWithNotRequiredArrayParameterWithUniqueItems,
            dataClassWithNotRequiredArrayParameterWithUniqueItemsComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithNotRequiredArrayParameterWithUniqueItems.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    @Test
    fun `Generate data class with required array parameter with unique items`() {
        val fileSpec = createModelComponent(
            dataClassWithRequiredArrayParameterWithUniqueItems,
            dataClassWithRequiredArrayParameterWithUniqueItemsComponents
        ).toString()

        val referenceContent = getReferenceContent("DataClassWithRequiredArrayParameterWithUniqueItems.kt")

        assertGeneratedMatch(referenceContent, fileSpec)
    }

    private fun getReferenceContent(fileName: String): String {
        val path = "src/test/kotlin/gen/routing/model/$fileName"
        return File(path).readText().replace("\r\n", "\n")
    }

    private fun assertGeneratedMatch(referenceContent: String, fileSpec: String) {
        val cleanedReferenceContent = referenceContent.replace("\n", "").replace(" ", "")
        val cleanedFileSpec = fileSpec.replace("\n", "").replace(" ", "")

        assertEquals(cleanedReferenceContent, cleanedFileSpec)
    }
}
