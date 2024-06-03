package gen.routing.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DataClassWithNotRequiredParameters(
  public val name: String? = null,
  public val age: Int? = null,
)
