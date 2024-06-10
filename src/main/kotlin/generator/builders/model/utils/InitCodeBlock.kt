package generator.builders.model.utils

import com.squareup.kotlinpoet.CodeBlock
import datamodel.Schema
import datamodel.Parameter
import datamodel.DataType

fun getInitCodeBlock(schema: Schema): CodeBlock {
    val codeBlock = CodeBlock.builder()
    val statements = mutableListOf<String>()

    schema.parameters.forEach { componentProperty ->
        statements.addAll(getPropertyStatements(componentProperty))
    }

    statements.forEach { statement ->
        codeBlock.addStatement(statement)
    }

    return codeBlock.build()
}

private fun getPropertyStatements(parameter: Parameter): List<String> {
    val statements = mutableListOf<String>()

    when (parameter.dataType) {
        DataType.INTEGER, DataType.DOUBLE, DataType.FLOAT, DataType.LONG, DataType.NUMBER, DataType.INT -> {
            statements.addAll(getNumberPropertyStatements(parameter))
        }

        DataType.STRING -> {
            statements.addAll(getStringPropertyStatements(parameter))
        }

        DataType.ARRAY -> {
            statements.addAll(getArrayPropertyStatements(parameter))
        }

        else -> {}
    }
    return statements
}


