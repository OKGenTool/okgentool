package generator.builders.client.utils

import com.squareup.kotlinpoet.FunSpec
import datamodel.*

fun getOperationRequestFunction(operation: DSLOperation, schemaNames: List<String>): FunSpec {
    val funSpec = FunSpec.builder(operation.name)
        .addParameters(getFunctionParameters(operation, schemaNames))
        .returns(getReturnType(operation.responses))

    return funSpec.build()
}
