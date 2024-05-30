package datamodel

import io.ktor.http.*

data class DSLOperation(
    val name: String,
    val requestBody: Body?,
    val responses: List<ResponseNew>?,
    val method: HttpMethod,
    val path: String,
    val summary: String,
    val parameters: List<DSLParameter>? = null,
)
