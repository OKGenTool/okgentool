package generator.model

import com.squareup.kotlinpoet.TypeName

data class ClientFunctionParameter(
    val name: String,
    val dataType: TypeName,
    val isQuery: Boolean = false,
    val isHeader: Boolean = false,
    val isPath: Boolean = false,
    val isBody: Boolean = false,
    val isList: Boolean = false,
    val bodyContentType: ContentType? = null,
) {
    init {
        require(isQuery || isHeader || isPath || isBody) {
            "Invalid ClientFunctionParameter"
        }
    }
}
