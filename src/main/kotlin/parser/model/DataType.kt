package parser.model

enum class DataType (val type: String, val format: String) {
    INTEGER("integer", "int32"),
    LONG("integer", "int64"),
    FLOAT("number", "float"),
    DOUBLE("number", "double"),
    STRING("string", ""),
    BYTE("string", "byte"),
    BINARY("string", "binary"),
    BOOLEAN("boolean", ""),
    DATE("string", "date"),
    DATE_TIME("string", "date-time"),
    PASSWORD("string", "password"),

    NUMBER("number", ""),
    ARRAY("array", ""),
    OBJECT("object", "");

    companion object {
        fun fromString(type: String, format: String): DataType? {
            return entries.firstOrNull { it.type == type && (it.format == format || it.format == "") }
        }

        fun getFormats(type: String): List<String> {
            return entries.filter { it.type == type }.map { it.format }.distinct()
        }
    }
}