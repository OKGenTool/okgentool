package gen.routing.model

import kotlin.Int
import kotlinx.serialization.Serializable

@Serializable
public data class dataClassWithNotRequiredIntParameterWithMaxAndMin(
  public val `value`: Int?,
) {
  init {
    require(value == null || value <= 10) { "value must be less than or equal to 10" }
    require(value == null || value >= 1) { "value must be greater than or equal to 1" }
  }
}
