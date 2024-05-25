package gen.routing.model

import kotlin.Int
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithNotRequiredIntParameterWithMultipleOf(
  public val `value`: Int?,
) {
  init {
    require(value == null || value % 2 == 0) { "value must be a multiple of 2" }
  }
}
