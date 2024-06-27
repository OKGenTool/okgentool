package generator.builders.client.utils

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import datamodel.*

fun getOperationRequestFunction(operation: DSLOperation, schemaNames: List<String>): FunSpec {
    val (parametersSpec, parametersObject) = getFunctionParameters(operation, schemaNames)
    val (returnTypeSpec, returnTypeName) = getReturnType(operation.responses)

    val funSpec = FunSpec.builder(operation.name)
        .addModifiers(KModifier.SUSPEND)
        .addParameters(parametersSpec)
        .returns(returnTypeSpec)
        .addCode(getCodeBlock(operation, parametersObject, returnTypeName))

    return funSpec.build()
}
