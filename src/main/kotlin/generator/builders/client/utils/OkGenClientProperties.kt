package generator.builders.client.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec

fun getOkGenClientProperties(): List<PropertySpec> {
    val props = mutableListOf<PropertySpec>()

    props.add(
        PropertySpec.builder("baseURL", String::class)
        .initializer("baseURL")
        .addModifiers(KModifier.PRIVATE)
        .build())

    props.add(
        PropertySpec.builder("client", ClassName("io.ktor.client", "HttpClient"))
        .initializer(
            """
            |HttpClient(engineFactory) {
            |    install(ContentNegotiation) {
            |        json()
            |        xml()
            |    }
            |    defaultRequest {
            |        url(baseURL)
            |    }
            |    apply(clientAdditionalConfigurations)
            |}
            """.trimMargin())
        .addModifiers(KModifier.PRIVATE)
        .build())

    return props
}