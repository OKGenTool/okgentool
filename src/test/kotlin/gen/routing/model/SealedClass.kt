package gen.routing.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class SealedClass

@Serializable
@SerialName("Child1")
public data class Child1(
  public val name: String,
) : SealedClass()

@Serializable
@SerialName("Child2")
public data class Child2(
  public val age: Int,
) : SealedClass()
