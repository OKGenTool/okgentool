package datamodel

data class CliModel(
    val sourcePath: String = "",
    val serverDestinationPath: String = "",
    val clientDestinationPath: String = "",
    val isServer: Boolean = false,
    val isClient: Boolean = false,
)