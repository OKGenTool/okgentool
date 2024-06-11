package parser.builders

import datamodel.*
import datamodel.Response.*
import generator.capitalize
import io.ktor.http.*
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponses
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("OperationsBuilder.kt")

private val dslOperations = mutableListOf<DSLOperation>()

fun buildOperations(): List<DSLOperation> {
    for (paths in openAPI.paths) {
        val pathItem = paths.value
        addOperation(pathItem.get, paths.key, "get")
        addOperation(pathItem.post, paths.key, "post")
        addOperation(pathItem.put, paths.key, "put")
        addOperation(pathItem.patch, paths.key, "patch")
        addOperation(pathItem.head, paths.key, "head")
        addOperation(pathItem.delete, paths.key, "delete")
        addOperation(pathItem.options, paths.key, "options")
        addOperation(pathItem.trace, paths.key, "trace")
    }
    return dslOperations
}

private fun addOperation(operation: Operation?, path: String, method: String) {
    if (operation == null) return

    val operationName = getOperationName(operation, path, method)
    val parameters = getParameters(operation)
    val inlineSchemas: MutableList<InlineSchema> = mutableListOf() //TODO delete this

    val dslOperation = DSLOperation(
        operationName,
        getBodyNew(operation),
        getResponses(operation.responses, operationName, inlineSchemas),
        HttpMethod.parse(method),
        path,
        operation.summary,
        operation.description,
        parameters,
        inlineSchemas
    )

    dslOperations.add(dslOperation)
}

private fun getParameters(operation: Operation): List<DSLParameter>? {
    return operation.parameters?.takeIf { it.isNotEmpty() }?.mapNotNull {
        when (it.`in`) {
            In.QUERY.value -> {
                if (it.schema.enum != null) {
                    QueryParameterEnum(
                        it.name,
                        it.description,
                        it.explode,
                        it.schema.enum
                    )
                } else if (it.schema.type == "array") {
                    QueryParameterArray(
                        it.name,
                        it.description,
                        it.explode,
                        DataType.fromString(it.schema.items.type, it.schema.items.format)
                    )
                } else if (it.schema.type == "string") {
                    QueryParameterSingle(
                        it.name,
                        it.description
                    )
                } else {
                    logger.warn("${operation.operationId}: Parameter not supported: $it")
                    null
                }
            }

            In.PATH.value -> {
                PathParameter(
                    it.name,
                    it.description,
                    DataType.fromString(it.schema.type, it.schema.format)
                )
            }

            In.HEADER.value -> {
                HeaderParameter(
                    it.name,
                    it.description
                )
            }

            else -> {
                logger.warn("${operation.operationId}: Parameter not supported: $it")
                null
            }
        }
    }
}

private fun getResponses(
    apiResponses: ApiResponses,
    operationName: String,
    inlineSchemas: MutableList<InlineSchema>, //TODO delete this
): List<Response> {
    val responses: MutableList<Response> = mutableListOf()

    apiResponses.map { response ->
        val content = response.value.content
        when (content) {
            null -> responses.add(ResponseNoContent(response.key, response.value.description))
            else -> {
                val schema = content[content.keys.first()]?.schema //TODO add all schemas, not the first only
                //For responses using reusable schemas
                schema?.`$ref`?.let {
                    responses.add(
                        ResponseRef(
                            schema.`$ref`,
                            response.key,
                            response.value.description
                        )
                    )
                }

                schema?.type?.let {
                    if (schema.type != "array") {
                        //Inline  Response
                        responses.add(
                            ResponseInline(
                                operationName,
                                response.key,
                                response.value.description,
                                DataType.fromString(schema.type, schema.format)
                            )
                        )
//                        responses.add(
//                            ResponseUnsupported(
//                                operationName,
//                                response.key,
//                                response.value.description
//                            )
//                        )
                    } else {
                        //For responses using arrays of reusable schemas
                        responses.add(
                            ResponseRefColl(
                                schema.items.`$ref`,
                                response.key,
                                response.value.description
                            )
                        )
                    }
                }

            }
        }
    }


    return responses
}


//private fun getResponses(
//    apiResponses: ApiResponses,
//    operationName: String,
//    inlineSchemas: MutableList<InlineSchema>,
//): List<Response> {
//    val responses: MutableList<Response> = mutableListOf()
//
//    for (response in apiResponses) {
//        val content = response.value.content
//
//        //TODO if schema is an inline object, it's need to add it to data model
//        var schemaRef: String? = null
//        content?.let {
//            val schema = it[content.keys.first()]?.schema //TODO add all schemas, not the first only
//            if (schema?.type == "object")
//                inlineSchemas.add(
//                    InlineSchema(
//                        "${operationName.capitalize()}Obj",
//                        schema
//                    )
//                )
//            else
//                schemaRef = getSchemaProp(schema, SchemaProps.REF)
//        }
//
//        val response = Response(
//            response.key,
//            response.value.description,
//            content?.keys?.toList(),
//            schemaRef,
//        )
//
//        responses.add(response)
//    }
//    return responses
//}

/**
 * If it's a valid operation, return the operation name based on OperationId.
 * If OperationId is not defined, return a composed name with the Path and Method
 */
private fun getOperationName(operation: Operation, path: String, method: String): String {
    if (operation.operationId != null && operation.operationId.isNotEmpty()) {
        return operation.operationId
    }
    val operationName = getComposedOperationName(path, method)
    logger.warn("Invalid or missing OperationID. Operation name defined as: $operationName")
    return operationName
}

private fun getComposedOperationName(path: String, method: String): String {
    val pathSplit = path.split("/")
    var finalPath = ""

    for (segment in pathSplit) {
        val editSeg = segment
            .removeSurrounding("{", "}")
            .capitalize()
        finalPath += editSeg
    }
    return "${method}$finalPath"
}

private fun getBodyNew(operation: Operation): Body? {
    val requestBody = operation.requestBody ?: return null

    val contentTypes = requestBody.content.keys.toList()

    val schema = requestBody.content[contentTypes.first()]?.schema
    val schemaRef = SchemaProps.getSchemaProp(schema, SchemaProps.REF)

    // If the schema as a $ref
    if (schemaRef != null) {
        return getBodyRef(schemaRef, contentTypes)
    } else {
        //The schema is not a $ref
        val type = SchemaProps.getSchemaProp(schema, SchemaProps.TYPE)

        //The schema is a collection?
        if (type == "array") {
            //Get Array Type: Could be an array of ref or pojo
            var className = schema?.items?.`$ref`
            if (className != null) {
                return BodyCollRef(contentTypes, SchemaProps.getRefSimpleName(className))
            } else {
                //It is an array of POJOs
                return getBodyCollPojo(operation.tags, schema, contentTypes)
            }
        }

        //The schema is a regular pojo
        return getBodyObj(schema, contentTypes)
    }
}

fun getBodyCollPojo(tags: List<String>?, schema: Schema<Any>?, contentTypes: List<String>): BodyCollPojo {
    val type = SchemaProps.getSchemaProp(schema, SchemaProps.ARRAYTYPE)
    val format = SchemaProps.getSchemaProp(schema, SchemaProps.ARRAYFORMAT)
    val dataType = DataType.fromString(type!!, format)
    return BodyCollPojo(contentTypes, tags, dataType!!)
}

fun getBodyObj(schema: Schema<Any>?, contentTypes: List<String>): BodyObj {
    val type = SchemaProps.getSchemaProp(schema, SchemaProps.TYPE)
    val format = SchemaProps.getSchemaProp(schema, SchemaProps.FORMAT)
    val dataType = DataType.fromString(type!!, format)
    return BodyObj(contentTypes, dataType!!)
}

fun getBodyRef(schemaRef: String, contentTypes: List<String>): BodyRef {
    return BodyRef(
        contentTypes,
        SchemaProps.getRefSimpleName(schemaRef)
    )
}


