package gen.routing.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public sealed class SealedClass

@Serializable
public data class Child1(
  public val name: String,
) : SealedClass()

@Serializable
public data class Child2(
  public val age: Int,
) : SealedClass()
