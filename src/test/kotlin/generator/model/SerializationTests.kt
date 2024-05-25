package generator.model

import gen.routing.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class SerializationTests {

    @Test
    fun `Serialize data class`() {
        val dataClass = DataClass("test", 5)

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClass>(jsonString)

        assertEquals("{\"name\":\"test\",\"age\":5}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize data class with array`() {
        val dataClass = DataClassWithArray("test", listOf(1, 2, 3))

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClassWithArray>(jsonString)

        assertEquals("{\"name\":\"test\",\"values\":[1,2,3]}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize data class with bi dimensional array`() {
        val dataClass = DataClassWithBiDimensionalArray(listOf(listOf(1, 2), listOf(3, 4)))

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClassWithBiDimensionalArray>(jsonString)

        assertEquals("{\"values\":[[1,2],[3,4]]}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize data class with enum parameter`() {
        val dataClass = DataClassWithEnumParameter("test", DataClassWithEnumParameterValue.A)

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClassWithEnumParameter>(jsonString)

        assertEquals("{\"name\":\"test\",\"value\":\"A\"}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize data class with generated data type`() {
        val dataClass = DataClassWithGeneratedDataType("test", DataClass("test", 2))

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClassWithGeneratedDataType>(jsonString)

        assertEquals("{\"name\":\"test\",\"value\":{\"name\":\"test\",\"age\":2}}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize data class with generated data type array`() {
        val dataClass = DataClassWithGeneratedTypeArray("test", listOf(DataClass("test", 2)))

        val jsonString = Json.encodeToString(dataClass)
        val deserialized = Json.decodeFromString<DataClassWithGeneratedTypeArray>(jsonString)

        assertEquals("{\"name\":\"test\",\"value\":[{\"name\":\"test\",\"age\":2}]}", jsonString)
        assertEquals(dataClass, deserialized)
    }

    @Test
    fun `Serialize sealed class`() {
        val child1 = Child1("test")
        val child2 = Child2(5)

        val jsonString1 = Json.encodeToString(SealedClass.serializer(), child1)
        val jsonString2 = Json.encodeToString(SealedClass.serializer(), child2)
        val deserialized1 = Json.decodeFromString<SealedClass>(jsonString1)
        val deserialized2 = Json.decodeFromString<SealedClass>(jsonString2)

        assertEquals("""{"type":"Child1","name":"test"}""", jsonString1)
        assertEquals("""{"type":"Child2","age":5}""", jsonString2)
        assertEquals(child1, deserialized1)
        assertEquals(child2, deserialized2)
    }
}