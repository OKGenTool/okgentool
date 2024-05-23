package datamodel

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(ResponseNew::class.java.simpleName)

data class ResponseNew(
    val statusCodeStr: String,
    val description: String,
    val contentTypes: List<String>?,
    val schemaRef: String?,
) {
    val statusCodeInt: Int

    init {
        if (statusCodeStr == "default") {
            statusCodeInt = 200
            logger.info("Mapping default status code to 200")
        } else
            statusCodeInt = statusCodeStr.toInt()
    }
}
