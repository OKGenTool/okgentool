package datamodel

enum class HttpMethods(val methodName: String) {
    GET("get"),
    HEAD("head"),
    POST("post"),
    PUT("put"),
    DELETE("delete"),
    OPTIONS("options"),
    TRACE("trace"),
    PATCH("patch");

    companion object {
        fun fromString(method: String): HttpMethods {
            return entries.firstOrNull { it.methodName == method }
                ?: throw IllegalArgumentException("Invalid method: $method")
        }
    }
}
