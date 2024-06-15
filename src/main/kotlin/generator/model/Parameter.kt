package generator.model

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName

/**
 * This data class is specific for the Generator block
 * Is not the same as DSLParameter, that is used in modeling the data
 * in the Data Model block
 */
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