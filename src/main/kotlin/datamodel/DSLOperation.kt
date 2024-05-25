package datamodel

import io.ktor.http.*

data class DSLOperation(
    val name: String,
    val requestBody: BodyNew?,
    val responses: List<ResponseNew>?,
    val method:HttpMethod
)
