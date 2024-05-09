package datamodel

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import java.time.LocalDate
import java.time.LocalDateTime

enum class DataType (val type: String, val format: String, val kotlinType: ClassName) {
    INTEGER("integer", "int32", Int::class.asTypeName()),
    LONG("integer", "int64", Long::class.asTypeName()),
    FLOAT("number", "float", Float::class.asTypeName()),
    DOUBLE("number", "double", Double::class.asTypeName()),
    STRING("string", "", String::class.asTypeName()),
    BYTE("string", "byte", Byte::class.asTypeName()),
    BINARY("string", "binary", ByteArray::class.asTypeName()),
    BOOLEAN("boolean", "", Boolean::class.asTypeName()),
    DATE("string", "date", LocalDate::class.asTypeName()),
    DATE_TIME("string", "date-time", LocalDateTime::class.asTypeName()),
    PASSWORD("string", "password", String::class.asTypeName()),

    NUMBER("number", "", Double::class.asTypeName()),
    ARRAY("array", "", List::class.asTypeName()),
    OBJECT("object", "", Any::class.asTypeName());

    companion object {
        fun fromString(type: String, format: String): DataType {
            return entries.firstOrNull { it.type == type && (it.format == format || it.format == "") }
                ?: OBJECT
        }

        fun getFormats(type: String): List<String> {
            return entries.filter { it.type == type }.map { it.format }.distinct()
        }
    }
}

fun getInitializerForType(dataType: DataType): String {
    return when (dataType) {
        DataType.STRING -> "%S"
        DataType.INTEGER, DataType.LONG, DataType.FLOAT, DataType.DOUBLE -> "%L"
        DataType.BOOLEAN -> "%L"
        else -> "%T()"
    }
}