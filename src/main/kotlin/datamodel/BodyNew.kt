package datamodel

import kotlin.reflect.KClass

abstract class BodyNew(
    open val returnType: List<String>,
)

/**
 * Body based on a $ref
 */
data class BodyRef(
    override val returnType: List<String>,
    val schemaRef: String,
) : BodyNew(returnType)

/**
 * Body based on a POJO
 */
data class BodyObj(
    override val returnType: List<String>,
    val dataType: DataType,
) : BodyNew(returnType)

/**
 * Body based on a Collection of $ref
 */
data class BodyCollRef(
    override val returnType: List<String>,
    val className: String,
) : BodyNew(returnType)

/**
 * Body based on a collection of POJOs
 */
data class BodyCollPojo(
    override val returnType: List<String>,
    val dataType: DataType,
) : BodyNew(returnType)
