package generator.model

import gen.routing.model.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith

class InitCodeBlockTests {

    @Test
    fun `Array with minimum and maximum items`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must have a minimum length of 2"
        ) {
            val failMin = DataClassWithRequiredArrayParameterWithMinMaxItems(
                listOf(1)
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must have a maximum length of 10"
        ) {
            val failMax = DataClassWithRequiredArrayParameterWithMinMaxItems(
                listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
            )
        }

        val success = DataClassWithRequiredArrayParameterWithMinMaxItems(
            listOf(1, 2)
        )

        assertEquals(listOf(1, 2), success.value)
    }

    @Test
    fun `Array with unique items`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must have unique items"
        ) {
            val fail = DataClassWithRequiredArrayParameterWithUniqueItems(
                listOf(1, 1)
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must have unique items"
        ) {
            val fail = DataClassWithNotRequiredArrayParameterWithUniqueItems(
                listOf(1, 2, 1)
            )
        }

        val success1 = DataClassWithRequiredArrayParameterWithUniqueItems(
            listOf(1, 2)
        )
        val success2 = DataClassWithNotRequiredArrayParameterWithUniqueItems(
            listOf(1, 2)
        )

        assertEquals(listOf(1, 2), success1.value)
        assertEquals(listOf(1, 2), success2.value)
    }

    @Test
    fun `Int with minimum and maximum`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must be greater than or equal to 1"
        ) {
            val failMin = dataClassWithRequiredIntParameterWithMaxAndMin(
                0
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be less than or equal to 10"
        ) {
            val failMax = dataClassWithRequiredIntParameterWithMaxAndMin(
                11
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be greater than or equal to 1"
        ) {
            val failMin = dataClassWithNotRequiredIntParameterWithMaxAndMin(
                0
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be less than or equal to 10"
        ) {
            val failMax = dataClassWithNotRequiredIntParameterWithMaxAndMin(
                11
            )
        }

        val success1 = dataClassWithRequiredIntParameterWithMaxAndMin(
            2
        )
        val success2 = dataClassWithNotRequiredIntParameterWithMaxAndMin(
            2
        )
        val success3 = dataClassWithNotRequiredIntParameterWithMaxAndMin(
            null
        )

        assertEquals(2, success1.value)
        assertEquals(2, success2.value)
        assertEquals(null, success3.value)
    }

    @Test
    fun `Int with exclusive minimum and maximum`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must be greater than 1"
        ) {
            val failMin = dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin(
                1
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be greater than 1"
        ) {
            val failMin = dataClassWithRequiredIntParameterWithExclusiveMaxAndMin(
                1
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be less than 10"
        ) {
            val failMax = dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin(
                10
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be less than 10"
        ) {
            val failMax = dataClassWithRequiredIntParameterWithExclusiveMaxAndMin(
                10
            )
        }

        val success1 = dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin(
            2
        )

        val success2 = dataClassWithRequiredIntParameterWithExclusiveMaxAndMin(
            2
        )

        val success3 = dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin(
            null
        )

        assertEquals(2, success1.value)
        assertEquals(2, success2.value)
        assertEquals(null, success3.value)
    }

    @Test
    fun `Int with multipleOf constraints`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must be a multiple of 2"
        ) {
            val fail = DataClassWithNotRequiredIntParameterWithMultipleOf(
                3
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must be a multiple of 2"
        ) {
            val fail = DataClassWithRequiredIntParameterWithMultipleOf(
                3
            )
        }

        val success1 = DataClassWithNotRequiredIntParameterWithMultipleOf(
            4
        )

        val success2 = DataClassWithRequiredIntParameterWithMultipleOf(
            4
        )

        val success3 = DataClassWithNotRequiredIntParameterWithMultipleOf(
            null
        )

        assertEquals(4, success1.value)
        assertEquals(4, success2.value)
        assertEquals(null, success3.value)
    }

    @Test
    fun `String with minLength and maxLength constraints`() {
        assertFailsWith<IllegalArgumentException>(
            message = "value must have a minimum length of 2"
        ) {
            val failMin = DataClassWithRequiredStringParameterWithMinMaxLen(
                "a"
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must have a maximum length of 10"
        ) {
            val failMax = DataClassWithRequiredStringParameterWithMinMaxLen(
                "12345678901"
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must have a minimum length of 2"
        ) {
            val failMin = DataClassWithNotRequiredStringParameterWithMinMaxLen(
                "a"
            )
        }

        assertFailsWith<IllegalArgumentException>(
            message = "value must have a maximum length of 10"
        ) {
            val failMax = DataClassWithNotRequiredStringParameterWithMinMaxLen(
                "12345678901"
            )
        }

        val success1 = DataClassWithRequiredStringParameterWithMinMaxLen(
            "ab"
        )
        val success2 = DataClassWithNotRequiredStringParameterWithMinMaxLen(
            "ab"
        )
        val success3 = DataClassWithNotRequiredStringParameterWithMinMaxLen(
            null
        )

        assertEquals("ab", success1.value)
        assertEquals("ab", success2.value)
        assertEquals(null, success3.value)
    }

    // TODO: Implement tests for string pattern
}