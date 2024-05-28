package gen.routing.model

import kotlin.Int
import kotlinx.serialization.Serializable

@Serializable
public data class dataClassWithRequiredIntParameterWithExclusiveMaxAndMin(
  public val `value`: Int,
) {
  init {
    require(value < 10) { "value must be less than 10" }
    require(value > 1) { "value must be greater than 1" }
  }
}
