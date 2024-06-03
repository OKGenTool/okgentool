package gen.routing.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithNotRequiredArrayParameterWithUniqueItems(
  public val `value`: List<Int>? = null,
) {
  init {
    require(
      value == null ||
              value.toSet().size == value.size
    ) {
      "value must have unique items"
    }
  }
}
