package datamodel

import io.ktor.http.*

data class DSLOperation(
    val name: String,
    val requestBody: Body?,
    val responses: List<Response>?,
    val method: HttpMethod,
    val path: String,
    val summary: String,
    val description: String,
    val parameters: List<DSLParameter>? = null,
)
