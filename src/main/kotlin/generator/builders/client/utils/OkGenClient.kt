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
        .addProperties(getOkGenClientProperties())
        .primaryConstructor(FunSpec.constructorBuilder()
            .addParameter("baseURL", String::class)
            .build()
        )

    for (operation in operations) {
        typeSpec.addFunction(getOperationRequestFunction(operation, schemaNames))
    }

    return typeSpec.build()
}
