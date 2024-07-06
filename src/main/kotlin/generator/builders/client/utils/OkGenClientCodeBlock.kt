package generator.builders.client.utils

import com.squareup.kotlinpoet.CodeBlock
import datamodel.DSLOperation
import generator.decapitalize
import generator.model.ClientFunctionParameter
import generator.model.ClientResponse
import generator.model.ContentType

fun getCodeBlock(operation: DSLOperation, parametersObject: List<ClientFunctionParameter>, responseReturn: ClientResponse): CodeBlock {
    val codeBlock = CodeBlock.builder()

    codeBlock.beginControlFlow("try {")
    codeBlock.beginControlFlow("val response = client.${operation.method.value} {")
    codeBlock.beginControlFlow("url {")
    codeBlock.addStatement(getPathSegments(operation))

    val queryParameters = parametersObject.filter { it.isQuery }
    if (queryParameters.isNotEmpty()) {
        for (parameter in queryParameters) {
            codeBlock.beginControlFlow("if (${parameter.name} != null) {")
            if (parameter.isList) {
                codeBlock.addStatement("parameters.appendAll(\"${parameter.name}\", ${parameter.name})")
            } else {
                codeBlock.addStatement("parameters.append(\"${parameter.name}\", ${parameter.name})")
            }
            codeBlock.endControlFlow()
        }
    }
    codeBlock.endControlFlow()

    val bodyParameter = parametersObject.find { it.isBody }
    if (bodyParameter != null) {
        if (bodyParameter.bodyContentType == null) {
            codeBlock.addStatement("contentType(${ContentType.JSON.code})")
        } else {
            codeBlock.addStatement("contentType(${bodyParameter.bodyContentType.code})")
        }
        codeBlock.addStatement("setBody(${bodyParameter.name})")
    }
    codeBlock.endControlFlow()

    if (responseReturn.noContent) {
        codeBlock.addStatement("val ResponseStatusCode = response.status.value")
        codeBlock.addStatement("return getResponseState(null, ResponseStatusCode)")
    }

    if (responseReturn.dataType != null) {
        codeBlock.addStatement("val httpResponse = response.body<${if(!responseReturn.isList) responseReturn.dataType.name else "List<${responseReturn.dataType.name}>"}>()")
        codeBlock.addStatement("val responseStatusCode = response.status.value")
        codeBlock.addStatement("return getResponseState(httpResponse, responseStatusCode)")
    }

    if (responseReturn.schemaName != null) {
        codeBlock.addStatement("val httpResponse = response.body<${if(!responseReturn.isList) responseReturn.schemaName else "List<${responseReturn.schemaName}>"}>()")
        codeBlock.addStatement("val responseStatusCode = response.status.value")
        codeBlock.addStatement("return getResponseState(httpResponse, responseStatusCode)")
    }

    codeBlock.endControlFlow()

    codeBlock.beginControlFlow("catch (e: Exception) {")
    codeBlock.addStatement("return getResponseState(null, null)")
    codeBlock.endControlFlow()

    return codeBlock.build()
}

private fun getPathSegments(operation: DSLOperation): String {
    val pathSegments = operation.path.split("/").filter { it.isNotEmpty() }
    val path = StringBuilder()
    path.append("appendPathSegments(")

    val lastIndex = pathSegments.size - 1
    for ((index, segment) in pathSegments.withIndex()) {
        if (segment.startsWith("{") && segment.endsWith("}")) {
            path.append("${segment.substring(1, segment.length - 1)}.toString()")
        } else {
            path.append("\"$segment\"")
        }
        if (index != lastIndex) {
            path.append(", ")
        }
    }

    path.append(")")
    return path.toString()
}
