package datamodel

sealed class DSLParameter(
    open val name: String,
    open val `in`: In,
    open val description: String,
)

data class QueryParameterSingle(
    override val name: String,
    override val description: String,
) : DSLParameter(
    name,
    In.QUERY,
    description,
)

data class QueryParameterEnum(
    override val name: String,
    override val description: String,
    val explode: Boolean,
    val enum: List<Any>,
) : DSLParameter(
    name,
    In.QUERY,
    description,
)

data class QueryParameterArray(
    override val name: String,
    override val description: String,
    val explode: Boolean,
    val itemsType: DataType,
) : DSLParameter(
    name,
    In.QUERY,
    description,
)

data class PathParameter(
    override val name: String,
    override val description: String,
    val type: DataType,
) : DSLParameter(
    name,
    In.PATH,
    description,
)

data class HeaderParameter(
    override val name: String,
    override val description: String,
) : DSLParameter(
    name,
    In.HEADER,
    description,
)

//data class DSLParameterUnsupported(
//    val operationName: String,
//    override val name: String,
//    override val description: String,
//) : DSLParameter(
//    name,
//    In.QUERY,
//    description,
//)