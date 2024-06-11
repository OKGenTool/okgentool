package generator.model

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName

//TODO why do I need this class, when already have DSLParameter?
data class Parameter(
    val name: String,
    val type: TypeName,
    val visibility: Visibility = Visibility.PUBLIC,
    val enum: List<String>? = null,
)

enum class Visibility(val modifier: KModifier) {
    PRIVATE(KModifier.PRIVATE),
    PUBLIC(KModifier.PUBLIC)
}