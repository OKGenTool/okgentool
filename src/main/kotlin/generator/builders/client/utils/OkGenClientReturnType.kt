package generator.builders.client.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import datamodel.Response
import generator.capitalize
import generator.model.ClientResponse
import generator.model.Packages

fun getReturnType(responses: List<Response>?): Pair<TypeName, ClientResponse> {
    if (responses.isNullOrEmpty()) {
        return Pair(
            Any::class.asTypeName(),
            ClientResponse("Any", noContent = true)
        )
    }

    val responseState = ClassName(Packages.CLIENT, "ResponseState")

    val responseRef = responses.firstOrNull() { it is Response.ResponseRef } as Response.ResponseRef?
    if (responseRef != null) {
        val name = responseRef.schemaRef.substringAfterLast("/").capitalize()
        return Pair(
            responseState.parameterizedBy(
                ClassName(Packages.MODEL, name)
            ),
            ClientResponse(name, schemaName = name)
        )
    }

    val responseCollRef = responses.firstOrNull() { it is Response.ResponseRefColl } as Response.ResponseRefColl?
    if (responseCollRef != null) {
        val name = responseCollRef.schemaRef.substringAfterLast("/").capitalize()
        return Pair(
            responseState.parameterizedBy(List::class.asTypeName()
                .parameterizedBy(
                    ClassName(
                        Packages.MODEL,
                        name
                    )
                )
            ),
            ClientResponse(
                "List<${name}>",
                isList = true,
                schemaName = name
            )
        )
    }

    return Pair(
        responseState.parameterizedBy(Any::class.asTypeName()),
        ClientResponse(
            "List<Any>",
            noContent = true
        )
    )
    // TODO: Add support for Response.ResponseNoContent and ResponseInline
}