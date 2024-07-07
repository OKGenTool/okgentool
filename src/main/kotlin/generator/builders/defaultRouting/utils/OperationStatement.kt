package generator.builders.defaultRouting.utils

import com.squareup.kotlinpoet.CodeBlock
import datamodel.*
import generator.capitalize
import generator.decapitalize
import generator.model.Packages

fun createOperationStatement(operation: DSLOperation, schemas: List<Schema>): CodeBlock {
    val code = CodeBlock.builder()
    val successResponse = getSuccessResponse(operation, schemas)
    val errorResponse = getErrorResponse(operation, schemas)
    code.add("route.${operation.name} {\n")
    code.add("\t\ttry {\n")

    if (successResponse != null && successResponse is Response.ResponseRef) {
        val exampleName = "example${successResponse.schemaRef.split('/').last().capitalize()}"
        code.add("\t\t\tif ($exampleName != null) {\n")
        code.add("\t\t\t\tresponse.${operation.name.decapitalize()}Response${successResponse.statusCodeStr.capitalize()}($exampleName)\n")
        code.add("\t\t\t} else {\n")
        code.add("\t\t\t\tunsafe.respondNotImplemented()\n")
        code.add("\t\t\t}\n")
    } else {
        code.add("\t\t\tunsafe.respondNotImplemented()\n")
    }

    code.add("\t\t} catch (e: Exception) {\n")

    if (errorResponse != null) {
        code.add("\t\t\tresponse.${operation.name.decapitalize()}Response${errorResponse.statusCodeInt.toString()}()\n")
    } else {
        code.add("\t\t\tunsafe.respondBadRequest()\n")
    }
    code.add("\t\t}\n")

    code.add("}\n\n")

    return code.build()
}

private fun getErrorResponse(operation: DSLOperation, schemas: List<Schema>): Response? {
    if (operation.responses == null) {
        return null
    }

    for (response in operation.responses) {
        //response.setStatusCodeInt()
        if (response.statusCodeInt in 400..599) {
            return response
        }
    }

    return null
}

private fun getSuccessResponse(operation: DSLOperation, schemas: List<Schema>): Response? {
    if (operation.responses == null) {
        return null
    }

    for (response in operation.responses) {
//        response.setStatusCodeInt()
        if (response.statusCodeInt in 200..299) {
            return response
        }
    }

    return null
}
