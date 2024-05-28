package gen.routing.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithNotRequiredArrayParameterWithMinMaxItems(
  public val `value`: List<Int>,
) {
  init {
    require(value == null || value.size >= 2) { "value must have a minimum length of 2" }
    require(value == null || value.size <= 10) { "value must have a maximum length of 10" }
  }
}
