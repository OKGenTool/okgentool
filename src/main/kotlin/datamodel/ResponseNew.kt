package datamodel

data class ResponseNew(
    val statusCode: String,
    val description: String,
    val contentTypes: List<String>?,
    val schemaRef: String?,
)
//{
//    val ktorStatusCode: HttpStatusCode?
//
//    init {
//        if (statusCode.toIntOrNull() != null)
//            ktorStatusCode = HttpStatusCode
//                .fromValue(statusCode.toInt())
//                .description(description)
//        else
//            ktorStatusCode = null
//    }
//}


