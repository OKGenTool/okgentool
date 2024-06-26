package generator.builders.client.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import datamodel.Response
import generator.capitalize
import generator.model.Packages

fun getReturnType(responses: List<Response>?): TypeName {
    if (responses.isNullOrEmpty()) {
        return Any::class.asTypeName()
    }

    val responseState = ClassName(Packages.CLIENT, "ResponseState")

    val responseRef = responses.firstOrNull() { it is Response.ResponseRef } as Response.ResponseRef?
    if (responseRef != null) {
        return responseState.parameterizedBy(
            ClassName(Packages.MODEL, responseRef.schemaRef.substringAfterLast("/").capitalize())
        )
    }

    val responseCollRef = responses.firstOrNull() { it is Response.ResponseRefColl } as Response.ResponseRefColl?
    if (responseCollRef != null) {
        return responseState.parameterizedBy(List::class.asTypeName()
            .parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    responseCollRef.schemaRef.substringAfterLast("/").capitalize()
                )
            ))
    }

    return responseState.parameterizedBy(Any::class.asTypeName())
    // TODO: Add support for Response.ResponseNoContent and ResponseInline
}