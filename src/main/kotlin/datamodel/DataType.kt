package datamodel

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = LoggerFactory.getLogger(DataType::class.java.simpleName)

enum class DataType(val type: String, val format: String, val kotlinType: ClassName) {
    INTEGER("integer", "int32", Int::class.asTypeName()),
    LONG("integer", "int64", Long::class.asTypeName()),
    FLOAT("number", "float", Float::class.asTypeName()),
    DOUBLE("number", "double", Double::class.asTypeName()),
    STRING("string", "", String::class.asTypeName()),
    BYTE("string", "byte", ByteArray::class.asTypeName()),
    BINARY("string", "binary", ByteArray::class.asTypeName()),
    BOOLEAN("boolean", "", Boolean::class.asTypeName()),
    DATE("string", "date", LocalDate::class.asTypeName()),
    DATE_TIME("string", "date-time", LocalDateTime::class.asTypeName()),
    PASSWORD("string", "password", String::class.asTypeName()),

    INT("integer", "", Int::class.asTypeName()),
    NUMBER("number", "", Double::class.asTypeName()),
    ARRAY("array", "", List::class.asTypeName()),
    OBJECT("object", "", Any::class.asTypeName());

    companion object {
        fun fromString(type: String, format: String?): DataType {
            logger.debug("Get dataType for type: $type and format: $format")

            // Find exact match
            var dataType = entries.firstOrNull { it.type == type && it.format == format }

            // Find match with empty format
            if (dataType == null) {
                dataType = entries.firstOrNull { it.type == type && it.format == "" } ?: OBJECT
            }

            logger.debug("DataType found: $dataType")

            return dataType
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