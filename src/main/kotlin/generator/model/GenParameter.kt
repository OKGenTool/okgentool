package generator.model

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName

//TODO Rename this class
data class GenParameter(
    val name: String,
    val type: TypeName,
    val visibility: Visibility = Visibility.PUBLIC,
    val validations: List<String>? = null,
)

enum class Visibility(val modifier: KModifier) {
    PRIVATE(KModifier.PRIVATE),
    PUBLIC(KModifier.PUBLIC)
}