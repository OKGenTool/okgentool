package datamodel

abstract class BodyNew(
    open val contentTypes: List<String>,
)

/**
 * Body based on a $ref
 */
data class BodyRef(
    override val contentTypes: List<String>,
    val schemaRef: String,
) : BodyNew(contentTypes)

/**
 * Body based on a POJO
 */
data class BodyObj(
    override val contentTypes: List<String>,
    val dataType: DataType,
) : BodyNew(contentTypes)

/**
 * Body based on a Collection of $ref
 */
data class BodyCollRef(
    override val contentTypes: List<String>,
    val className: String,
) : BodyNew(contentTypes)

/**
 * Body based on a collection of POJOs
 */
data class BodyCollPojo(
    override val contentTypes: List<String>,
    val tags: List<String>?,
    val dataType: DataType,
) : BodyNew(contentTypes)
