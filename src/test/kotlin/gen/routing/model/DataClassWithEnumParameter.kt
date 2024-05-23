package gen.routing.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithEnumParameter(
  public val name: String,
  public val `value`: DataClassWithEnumParameterValue,
)

public enum class DataClassWithEnumParameterValue {
  A,
  B,
  C,
}
