package generator.builders.client.utils

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import generator.model.Imports
import generator.model.Imports.Companion.addCustomImport
import generator.model.Packages

fun createOkGenClientFile(operations: List<DSLOperation>, schemaNames: List<String>): FileSpec {
    return FileSpec.builder(Packages.CLIENT, "OkGenClient")
        .addType(createOkGenClientType(operations, schemaNames))
        .addCustomImport(Imports.KTOR_HTTP_CLIENT)
        .addCustomImport(Imports.KTOR_CLIENT_ENGINE_CIO)
        .addCustomImport(Imports.KTOR_CLIENT_DEFAULT_REQUEST)
        .addCustomImport(Imports.KTOR_CLIENT_CONTENT_NEGOTIATION)
        .addCustomImport(Imports.KTOR_SERIALIZATION_JSON)
        .addCustomImport(Imports.KTOR_SERIALIZATION_XML)
        .build()
}

fun createOkGenClientType(operations: List<DSLOperation>, schemaNames: List<String>): TypeSpec {
    val typeSpec = TypeSpec.classBuilder("OkGenClient")

    val baseUrlProperty = PropertySpec.builder("baseURL", String::class)
        .initializer("baseURL")
        .addModifiers(KModifier.PRIVATE)
        .build()

    val clientProperty = PropertySpec.builder("client", ClassName("io.ktor.client", "HttpClient"))
        .initializer(
            """
            |HttpClient(CIO) {
            |    install(ContentNegotiation) {
            |        json()
            |        xml()
            |    }
            |    defaultRequest {
            |        url(baseURL)
            |    }
            |}
            """.trimMargin())
        .addModifiers(KModifier.PRIVATE)
        .build()

    val constructorSpec = FunSpec.constructorBuilder()
        .addParameter("baseURL", String::class)
        .build()

    typeSpec.primaryConstructor(constructorSpec)
    typeSpec.addProperty(baseUrlProperty)
    typeSpec.addProperty(clientProperty)
    return typeSpec.build()
}
