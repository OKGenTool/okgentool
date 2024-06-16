package datamodel

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Response::class.java.simpleName)

sealed class Response(
    open val statusCodeStr: String,
    open val description: String,
    open val headers: List<DSLHeader>?
) {
    var statusCodeInt: Int? = null

    fun setStatusCodeInt() {
        if (statusCodeStr == "default") {
            statusCodeInt = 200
            logger.info("Mapping default status code to 200")
        } else statusCodeInt = statusCodeStr.toInt()
    }

    /**
     * Response with a referenced schema
     */
    data class ResponseRef(
        val schemaRef: String,
        override val statusCodeStr: String,
        override val description: String,
        override val headers: List<DSLHeader>?
    ) : Response(statusCodeStr, description, headers) {
        init {
            setStatusCodeInt()
        }
    }

    /**
     * Response with a collection of referenced schemas
     */
    data class ResponseRefColl(
        val schemaRef: String,
        override val statusCodeStr: String,
        override val description: String,
        override val headers: List<DSLHeader>?
    ) : Response(statusCodeStr, description, headers) {
        init {
            setStatusCodeInt()
        }
    }

    /**
     * Response with inline content (not using reusable schemas)
     */
    data class ResponseInline(
        val operationName: String,
        override val statusCodeStr: String,
        override val description: String,
        val type: DataType,
        override val headers: List<DSLHeader>?
    ) : Response(statusCodeStr, description, headers) {
        init {
            setStatusCodeInt()
        }
    }

    /**
     * Response with no content
     */
    data class ResponseNoContent(
        override val statusCodeStr: String,
        override val description: String,
        override val headers: List<DSLHeader>?
    ) : Response(statusCodeStr, description, headers) {
        init {
            setStatusCodeInt()
        }
    }
}
