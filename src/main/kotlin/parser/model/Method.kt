package parser.model

data class Method(
    val operationName: String,
    val parameters: List<Parameter>,
    val requestBody: Body?,
    val responses: List<Response>,
)