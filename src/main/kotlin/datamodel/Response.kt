package datamodel

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Response::class.java.simpleName)

sealed class Response(
    open val statusCodeStr: String,
    open val description: String,
) {
    var statusCodeInt: Int? = null

    fun setStatusCodeInt() {
        if (statusCodeStr == "default") {
            statusCodeInt = 200
            logger.info("Mapping default status code to 200")
        } else statusCodeInt = statusCodeStr.toInt()
    }

    data class ResponseRef(
        val schemaRef: String,
        override val statusCodeStr: String,
        override val description: String,
    ) : Response(statusCodeStr, description) {
        init {
            setStatusCodeInt()
        }
    }

    data class ResponseRefColl(
        val schemaRef: String,
        override val statusCodeStr: String,
        override val description: String,
    ) : Response(statusCodeStr, description) {
        init {
            setStatusCodeInt()
        }
    }

    data class ResponseInline(
        val operationName: String,
        override val statusCodeStr: String,
        override val description: String,
    ) : Response(statusCodeStr, description) {
        init {
            setStatusCodeInt()
        }
    }

    data class ResponseUnsupported(
        val operationName: String,
        override val statusCodeStr: String,
        override val description: String,
    ) : Response(statusCodeStr, description)
}
