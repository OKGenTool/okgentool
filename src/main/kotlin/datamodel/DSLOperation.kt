package datamodel

data class DSLOperation(
    val name: String,
    val requestBody: BodyNew?,
    val responses: List<ResponseNew>?,
)
