package generator.model

import datamodel.DataType

data class ClientResponse (
    val name: String,
    val isList: Boolean = false,
    val dataType: DataType? = null,
    val schemaName: String? = null,
    val noContent: Boolean = false
)