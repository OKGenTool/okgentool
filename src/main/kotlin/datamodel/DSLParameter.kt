package datamodel

data class DSLParameter(
    val name: String,
    val `in`: In,
    val description: String,
    val type: DataType,
    val subType: DataType?,
    val enum: MutableList<Any>?
)