package generator.builders.client.utils

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import datamodel.*

fun getOperationRequestFunction(operation: DSLOperation, schemaNames: List<String>): FunSpec {
    val (parametersSpec, parametersObject) = getFunctionParameters(operation, schemaNames)

    val funSpec = FunSpec.builder(operation.name)
        .addModifiers(KModifier.SUSPEND)
        .addParameters(parametersSpec)
        .returns(getReturnType(operation.responses))
        .addCode(getCodeBlock(operation, schemaNames, parametersObject))

    return funSpec.build()
}
