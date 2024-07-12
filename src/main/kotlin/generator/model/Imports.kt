package generator.model

import com.squareup.kotlinpoet.FileSpec

enum class Imports(
    val packageName: String,
    val names: String,
) {
    KTOR_SERVER_RESPOND("io.ktor.server.response", "respond"),
    KTOR_SERVER_POST("io.ktor.server.resources", "post"),
    KTOR_SERVER_PUT("io.ktor.server.resources", "put"),
    KTOR_SERVER_GET("io.ktor.server.resources", "get"),
    KTOR_SERVER_DELETE("io.ktor.server.resources", "delete"),
    KTOR_SERVER_RECEIVE("io.ktor.server.request", "receive"),
    KTOR_SERVER_REQUEST_HEADER("io.ktor.server.request", "header"),
    KTOR_SERVER_RESPONSE_HEADER("io.ktor.server.response", "header"),
    KTOR_HTTP_STATUS_CODE("io.ktor.http", "HttpStatusCode"),
    KTOR_APPLICATION_APPLICATIONCALL("io.ktor.server.application", "ApplicationCall"),
    KTOR_APPLICATION_CALL("io.ktor.server.application", "call"),
    KTOR_APPLICATION_APPLICATION("io.ktor.server.application", "Application"),
    KTOR_APPLICATION_INSTALL("io.ktor.server.application", "install"),
    KTOR_CONTENT_NEGOTIATION("io.ktor.server.plugins.contentnegotiation", "ContentNegotiation"),
    KTOR_SERIALIZATION_JSON("io.ktor.serialization.kotlinx.json", "json"),
    KTOR_SERIALIZATION_XML("io.ktor.serialization.kotlinx.xml", "xml"),
    KTOR_HTTP_CLIENT("io.ktor.client", "HttpClient"),
    KTOR_CLIENT_ENGINE_CIO("io.ktor.client.engine.cio", "CIO"),
    KTOR_CLIENT_DEFAULT_REQUEST("io.ktor.client.plugins", "defaultRequest"),
    KTOR_CLIENT_CONTENT_NEGOTIATION("io.ktor.client.plugins.contentnegotiation", "ContentNegotiation"),
    KTOR_CLIENT_REQUEST_GET("io.ktor.client.request", "get"),
    KTOR_CLIENT_REQUEST_POST("io.ktor.client.request", "post"),
    KTOR_CLIENT_REQUEST_PUT("io.ktor.client.request", "put"),
    KTOR_CLIENT_REQUEST_DELETE("io.ktor.client.request", "delete"),
    KTOR_CLIENT_REQUEST_SET_BODY("io.ktor.client.request", "setBody"),
    KTOR_HTTP_APPEND_PATH_SEGMENTS("io.ktor.http", "appendPathSegments"),
    KTOR_HTTP_CONTENT_TYPE("io.ktor.http", "ContentType"),
    KTOR_HTTP_CONTENT_TYPE_FUNCTION("io.ktor.http", "contentType"),
    KTOR_CLIENT_CALL_BODY("io.ktor.client.call", "body"),

    ;

    companion object {
        fun FileSpec.Builder.addCustomImport(import: Imports): FileSpec.Builder {
            return this.addImport(import.packageName, import.names)
        }
    }
}

