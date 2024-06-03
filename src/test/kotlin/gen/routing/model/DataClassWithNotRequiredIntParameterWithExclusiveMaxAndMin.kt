package gen.routing.model

import kotlin.Int
import kotlinx.serialization.Serializable

@Serializable
public data class dataClassWithNotRequiredIntParameterWithExclusiveMaxAndMin(
  public val `value`: Int? = null,
) {
  init {
    require(
      value == null ||
              value < 10
    ) {
      "value must be less than 10"
    }
    require(
      value == null ||
              value > 1
    ) {
      "value must be greater than 1"
    }
  }
}
