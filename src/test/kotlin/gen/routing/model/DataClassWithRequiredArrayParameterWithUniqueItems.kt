package gen.routing.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithRequiredArrayParameterWithUniqueItems(
  public val `value`: List<Int>,
) {
  init {
    require(value.toSet().size == value.size) { "value must have unique items" }
  }
}
