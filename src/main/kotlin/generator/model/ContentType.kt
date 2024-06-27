package generator.model

enum class ContentType(val description: String, val code: String) {
    JSON("application/json", "ContentType.Application.Json"),
    XML("application/xml", "ContentType.Application.Xml"),
}