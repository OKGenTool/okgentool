package generator.builders.client.utils

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.DSLOperation
import generator.model.Imports
import generator.model.Imports.Companion.addCustomImport
import generator.model.Packages
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

fun createOkGenClientFile(operations: List<DSLOperation>, schemaNames: List<String>): FileSpec {
    return FileSpec.builder(Packages.CLIENT, "OkGenClient")
        .indent("    ")
        .addTypeAlias(createHttpClientConfigBlockTypeAlias())
        .addType(createOkGenClientType(operations, schemaNames))
        .addCustomImport(Imports.KTOR_HTTP_CLIENT)
        .addCustomImport(Imports.KTOR_CLIENT_ENGINE_CIO)
        .addCustomImport(Imports.KTOR_CLIENT_DEFAULT_REQUEST)
        .addCustomImport(Imports.KTOR_CLIENT_CONTENT_NEGOTIATION)
        .addCustomImport(Imports.KTOR_SERIALIZATION_JSON)
        .addCustomImport(Imports.KTOR_SERIALIZATION_XML)
        .addCustomImport(Imports.KTOR_CLIENT_REQUEST_GET)
        .addCustomImport(Imports.KTOR_CLIENT_REQUEST_POST)
        .addCustomImport(Imports.KTOR_CLIENT_REQUEST_PUT)
        .addCustomImport(Imports.KTOR_CLIENT_REQUEST_DELETE)
        .addCustomImport(Imports.KTOR_CLIENT_REQUEST_SET_BODY)
        .addCustomImport(Imports.KTOR_HTTP_APPEND_PATH_SEGMENTS)
        .addCustomImport(Imports.KTOR_HTTP_CONTENT_TYPE)
        .addCustomImport(Imports.KTOR_HTTP_CONTENT_TYPE_FUNCTION)
        .addCustomImport(Imports.KTOR_CLIENT_CALL_BODY)
        .build()
}

fun createOkGenClientType(operations: List<DSLOperation>, schemaNames: List<String>): TypeSpec {
    val typeSpec = TypeSpec.classBuilder("OkGenClient")
        .addProperties(getOkGenClientProperties())
        .primaryConstructor(FunSpec.constructorBuilder()
            .addParameter("baseURL", String::class)
            .addParameter(
                ParameterSpec.builder("engineFactory", HttpClientEngineFactory::class.asTypeName().parameterizedBy(HttpClientEngineConfig::class.asTypeName()))
                    .defaultValue("CIO")
                    .build()
            )
            .addParameter(
                ParameterSpec.builder("clientAdditionalConfigurations", ClassName(Packages.CLIENT, "HttpClientConfigBlock"))
                    .defaultValue("{}")
                    .build()
            )
            .build()
        )

    for (operation in operations) {
        typeSpec.addFunction(getOperationRequestFunction(operation, schemaNames))
    }

    return typeSpec.build()
}

private fun createHttpClientConfigBlockTypeAlias(): TypeAliasSpec {
    return TypeAliasSpec
        .builder(
            "HttpClientConfigBlock",
            LambdaTypeName
                .get(receiver = ClassName("io.ktor.client", "HttpClientConfig")
                    .parameterizedBy(HttpClientEngineConfig::class.asTypeName()),
                    returnType = UNIT
                )
        )
        .build()
}
