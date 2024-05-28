package parser.builders

import datamodel.*
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponses
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("MethodBuilder.kt")


//TODO evaulate the usage of this builder with OperationsBuilder

fun getMethods(paths: List<Path>): List<Method> {
    logger.info("Reading Methods")
    val methods = mutableListOf<Method>()

    for (path in paths) {
        for (method in path.methods) {
            methods.add(getMethod(path, method))
        }
    }

    return methods
}

fun getMethod(path: Path, method: HttpMethods): Method {
    val methodItem = openAPI.paths[path.url]!!
    val operation = getOperation(methodItem, method)

    return Method(
        operationName = operation.operationId ?: "${method.methodName}_${path.url}",
        parameters = getParameters(operation.parameters),
        requestBody = getBody(operation.requestBody),
        responses = getResponses(operation.responses),
    )
}

fun getResponses(responses: ApiResponses?): List<Response> {
    if (responses == null) return emptyList()

    return responses.keys.map {
        val response = responses[it]
        Response(
            statusCode = it.toString(),
            returnTypes = response?.content?.keys?.toList() ?: emptyList(),
            schemaName = response?.content?.values?.firstOrNull()?.schema?.`$ref`
        )
    }
}

fun getBody(requestBody: RequestBody?): Body? {
    if (requestBody == null) return null

    val returnTypes = requestBody.content.keys.toList()
    val schemaName = requestBody.content[returnTypes.first()]?.schema?.`$ref` ?: ""

    return Body(schemaName, returnTypes)
}

fun getParameters(parameters: List<Parameter>?): List<datamodel.Parameter> {
    if (parameters.isNullOrEmpty()) return emptyList()

    return parameters.map {
        Parameter(
            name = it.name,
            `in` = In.fromValue(it.`in`.toString()),
            required = it.required ?: false,
            dataType = DataType.fromString(it.schema.type, it.schema?.format ?: ""),
            allowEmptyValue = it.allowEmptyValue ?: false,
            items = null, // TODO
            default = it.schema?.default?.toString(), // TODO
            maximum = it.schema?.maximum?.toInt(),
            exclusiveMaximum = it.schema?.exclusiveMaximum ?: false,
            minimum = it.schema?.minimum?.toInt(),
            exclusiveMinimum = it.schema?.exclusiveMinimum ?: false,
            maxLength = it.schema?.maxLength,
            minLength = it.schema?.minLength,
            pattern = it.schema?.pattern,
            maxItems = it.schema?.maxItems,
            minItems = it.schema?.minItems,
            uniqueItems = it.schema?.uniqueItems ?: false,
            enum = it.schema?.enum?.toString(),
            multipleOf = it.schema?.multipleOf?.toInt()
        )
    }
}

private fun getOperation(methodItem: PathItem, method: HttpMethods): Operation {
    return when (method) {
        HttpMethods.GET -> methodItem.get
        HttpMethods.POST -> methodItem.post
        HttpMethods.PUT -> methodItem.put
        HttpMethods.DELETE -> methodItem.delete
        HttpMethods.OPTIONS -> methodItem.options
        HttpMethods.HEAD -> methodItem.head
        HttpMethods.PATCH -> methodItem.patch
        HttpMethods.TRACE -> methodItem.trace
    }
}
