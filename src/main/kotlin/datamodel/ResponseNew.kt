package datamodel

data class ResponseNew(
    val statusCode: String,
    val description: String,
    val contentTypes: List<String>?,
    val schemaRef: String?
)
