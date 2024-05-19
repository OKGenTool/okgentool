package parser.builders

import datamodel.*
import generator.capitalize
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.RequestBody
import org.slf4j.LoggerFactory
import parser.openAPI

private val logger = LoggerFactory.getLogger("OperationsBuilder.kt")

private val dslOperations = mutableListOf<DSLOperation>()

fun getOperations(): List<DSLOperation> {
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

    if (operation.summary.equals("Creates list of users with given input array"))
        logger.info("Creates list of users with given input array") //TODO

    val dslOperation = DSLOperation(
        getOperationName(operation, path, method),
        getBodyNew(operation.requestBody),
    )

    dslOperations.add(dslOperation)
}

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

private fun getBodyNew(requestBody: RequestBody?): BodyNew? {
    if (requestBody == null) return null

    val returnTypes = requestBody.content.keys.toList()

    val schema = requestBody.content[returnTypes.first()]?.schema
    val schemaRef = SchemaProps.getSchemaProp(schema, SchemaProps.REF)
    // If the schema as a $ref
    if (schemaRef != null) {
        return getBodyRef(schemaRef, returnTypes)
    } else {
        //The schema is not a $ref
        val type = SchemaProps.getSchemaProp(schema, SchemaProps.TYPE)

        //The schema is a collection?
        if (type == "array") {
            //Get Array Type: Could be an array of ref or pojo
            var className = schema?.items?.`$ref`
            if (className != null) {
                return BodyCollRef(returnTypes, SchemaProps.getRefSimpleName(className))
            } else {
                //It is an array of POJOs
                return  getBodyCollPojo(schema, returnTypes)
            }
        }

        //The schema is a regular pojo
        return getBodyObj(schema, returnTypes)
    }
}

fun getBodyCollPojo(schema: Schema<Any>?, returnTypes: List<String>): BodyCollPojo {
    val type = SchemaProps.getSchemaProp(schema, SchemaProps.ARRAYTYPE)
    val format = SchemaProps.getSchemaProp(schema, SchemaProps.ARRAYFORMAT)
    val dataType = DataType.fromString(type!!, format)
    return BodyCollPojo(returnTypes, dataType!!)
}

fun getBodyObj(schema: Schema<Any>?, returnTypes: List<String>): BodyObj {
    val type = SchemaProps.getSchemaProp(schema, SchemaProps.TYPE)
    val format = SchemaProps.getSchemaProp(schema, SchemaProps.FORMAT)
    val dataType = DataType.fromString(type!!, format)
    return BodyObj(returnTypes, dataType!!)
}

fun getBodyRef(schemaRef: String, returnTypes: List<String>): BodyRef {
    return BodyRef(
        returnTypes,
        SchemaProps.getRefSimpleName(schemaRef)
    )
}


