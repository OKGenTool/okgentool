package generator.builders.model.utils

import datamodel.ComponentProperty
import generator.model.Statement

fun getNumberPropertyStatements(componentProperty: ComponentProperty): List<String> {
    val statements = mutableListOf<String>()

    componentProperty.maximum?.let { maximum ->
        if (componentProperty.required)
            if (componentProperty.exclusiveMaximum)
                statements.add(Statement.EXCLUSIVE_MAXIMUM.statement(maximum, componentProperty.name))
            else
                statements.add(Statement.MAXIMUM.statement(maximum, componentProperty.name))
        else
            if (componentProperty.exclusiveMaximum)
                statements.add(Statement.EXCLUSIVE_MAXIMUM_NULLABLE.statement(maximum, componentProperty.name))
            else
                statements.add(Statement.MAXIMUM_NULLABLE.statement(maximum, componentProperty.name))
    }

    componentProperty.minimum?.let { minimum ->
        if (componentProperty.required)
            if (componentProperty.exclusiveMinimum)
                statements.add(Statement.EXCLUSIVE_MINIMUM.statement(minimum, componentProperty.name))
            else
                statements.add(Statement.MINIMUM.statement(minimum, componentProperty.name))
        else
            if (componentProperty.exclusiveMinimum)
                statements.add(Statement.EXCLUSIVE_MINIMUM_NULLABLE.statement(minimum, componentProperty.name))
            else
                statements.add(Statement.MINIMUM_NULLABLE.statement(minimum, componentProperty.name))
    }

    componentProperty.multipleOf?.let { multipleOf ->
        if (componentProperty.required)
            statements.add(Statement.MULTIPLE_OF.statement(multipleOf, componentProperty.name))
        else
            statements.add(Statement.MULTIPLE_OF_NULLABLE.statement(multipleOf, componentProperty.name))
    }

    return statements
}

fun getStringPropertyStatements(componentProperty: ComponentProperty): List<String> {
    val statements = mutableListOf<String>()

    componentProperty.minLength?.let { minLength ->
        if (componentProperty.required)
            statements.add(Statement.MIN_LENGTH.statement(minLength, componentProperty.name))
        else
            statements.add(Statement.MIN_LENGTH_NULLABLE.statement(minLength, componentProperty.name))
    }

    componentProperty.maxLength?.let { maxLength ->
        if (componentProperty.required)
            statements.add(Statement.MAX_LENGTH.statement(maxLength, componentProperty.name))
        else
            statements.add(Statement.MAX_LENGTH_NULLABLE.statement(maxLength, componentProperty.name))
    }

    componentProperty.pattern?.let { pattern ->
        if (componentProperty.required)
            statements.add(Statement.PATTERN.statement(pattern, componentProperty.name))
        else
            statements.add(Statement.PATTERN_NULLABLE.statement(pattern, componentProperty.name))
    }

    return statements
}