package generator.builders.model.utils

import com.squareup.kotlinpoet.CodeBlock
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType

fun getInitCodeBlock(component: Component): CodeBlock {
    val codeBlock = CodeBlock.builder()
    val statements = mutableListOf<String>()

    component.parameters.forEach { componentProperty ->
        statements.addAll(getPropertyStatements(componentProperty))
    }

    statements.forEach { statement ->
        codeBlock.addStatement(statement)
    }

    return codeBlock.build()
}

private fun getPropertyStatements(componentProperty: ComponentProperties): List<String> {
    val statements = mutableListOf<String>()

    when (componentProperty.dataType) {
        DataType.INTEGER, DataType.DOUBLE, DataType.FLOAT, DataType.LONG, DataType.NUMBER -> {
            statements.addAll(getIntegerPropertyStatements(componentProperty))
        }

        else -> {}
    }
    return statements
}


