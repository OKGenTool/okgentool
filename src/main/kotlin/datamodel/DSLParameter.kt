package datamodel

data class DSLParameter(
    val name: String,
    val `in`: In,
    val description: String,
    val explode: Boolean,
    val type: DataType,
    val enum: List<Any>? = null,
    val default: Any? = null
)