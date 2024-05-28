package gen.routing.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithRequiredStringParameterWithMinMaxLen(
  public val `value`: String,
) {
  init {
    require(value.length >= 2) { "value must have a minimum length of 2" }
    require(value.length <= 10) { "value must have a maximum length of 10" }
  }
}
