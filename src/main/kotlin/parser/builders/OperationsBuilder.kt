package parser.builders

import datamodel.DSLOperation
import io.swagger.v3.oas.models.Operation
import org.slf4j.LoggerFactory
import parser.openAPI
import java.util.*

private val logger = LoggerFactory.getLogger("OperationsBuilder.kt")

private val dslOperations = mutableListOf<DSLOperation>()

fun getOperations(): List<DSLOperation> {
    for (paths in openAPI.paths) {
        val pathItem = paths.value
        getOperationName(pathItem.get, paths.key, "get")
        getOperationName(pathItem.post, paths.key, "post")
        getOperationName(pathItem.put, paths.key, "put")
        getOperationName(pathItem.patch, paths.key, "patch")
        getOperationName(pathItem.head, paths.key, "head")
        getOperationName(pathItem.delete, paths.key, "delete")
        getOperationName(pathItem.options, paths.key, "options")
        getOperationName(pathItem.trace, paths.key, "trace")
    }
    return dslOperations
}

/**
 * If it's a valid operation, save the operation name based on OperationId.
 * If OperationId is not defined, compose a name with the Path and Method
 */
private fun getOperationName(operation: Operation?, path: String, method: String) {
    if (operation != null) {
        if (operation.operationId != null && operation.operationId.isNotEmpty()) {
            dslOperations.add(DSLOperation(operation.operationId))
            return
        }
        val operationName = getComposedOperationName(path, method)
        logger.warn("Invalid or missing OperationID. Operation name defined as: $operationName")
        dslOperations.add(DSLOperation(operationName))
    }
}

private fun getComposedOperationName(path: String, method: String): String {
    val pathSplit = path.split("/")
    var finalPath = ""

    for (segment in pathSplit) {
        val editSeg = segment
            .removeSurrounding("{","}")
            .replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }
        finalPath += editSeg
    }
    return "${method}$finalPath"
}