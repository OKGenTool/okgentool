package generator.builders.model.utils

import datamodel.ArrayProperties
import datamodel.NumberProperties
import datamodel.Parameter
import datamodel.StringProperties
import generator.model.Statement

fun getNumberPropertyStatements(parameter: Parameter): List<String> {
    val statements = mutableListOf<String>()
    if (parameter.properties !is NumberProperties) return statements

    parameter.properties.maximum?.let { maximum ->
        if (parameter.required)
            if (parameter.properties.exclusiveMaximum)
                statements.add(Statement.EXCLUSIVE_MAXIMUM.statement(maximum, parameter.name))
            else
                statements.add(Statement.MAXIMUM.statement(maximum, parameter.name))
        else
            if (parameter.properties.exclusiveMaximum)
                statements.add(Statement.EXCLUSIVE_MAXIMUM_NULLABLE.statement(maximum, parameter.name))
            else
                statements.add(Statement.MAXIMUM_NULLABLE.statement(maximum, parameter.name))
    }

    parameter.properties.minimum?.let { minimum ->
        if (parameter.required)
            if (parameter.properties.exclusiveMinimum)
                statements.add(Statement.EXCLUSIVE_MINIMUM.statement(minimum, parameter.name))
            else
                statements.add(Statement.MINIMUM.statement(minimum, parameter.name))
        else
            if (parameter.properties.exclusiveMinimum)
                statements.add(Statement.EXCLUSIVE_MINIMUM_NULLABLE.statement(minimum, parameter.name))
            else
                statements.add(Statement.MINIMUM_NULLABLE.statement(minimum, parameter.name))
    }

    parameter.properties.multipleOf?.let { multipleOf ->
        if (parameter.required)
            statements.add(Statement.MULTIPLE_OF.statement(multipleOf, parameter.name))
        else
            statements.add(Statement.MULTIPLE_OF_NULLABLE.statement(multipleOf, parameter.name))
    }

    return statements
}

fun getStringPropertyStatements(parameter: Parameter): List<String> {
    val statements = mutableListOf<String>()
    if (parameter.properties !is StringProperties) return statements

    parameter.properties.minLength?.let { minLength ->
        if (parameter.required)
            statements.add(Statement.MIN_LENGTH.statement(minLength, parameter.name))
        else
            statements.add(Statement.MIN_LENGTH_NULLABLE.statement(minLength, parameter.name))
    }

    parameter.properties.maxLength?.let { maxLength ->
        if (parameter.required)
            statements.add(Statement.MAX_LENGTH.statement(maxLength, parameter.name))
        else
            statements.add(Statement.MAX_LENGTH_NULLABLE.statement(maxLength, parameter.name))
    }

//    componentProperty.properties.pattern?.let { pattern ->
//        if (componentProperty.required)
//            statements.add(Statement.PATTERN.statement(pattern, componentProperty.name))
//        else
//            statements.add(Statement.PATTERN_NULLABLE.statement(pattern, componentProperty.name))
//    }

    return statements
}

fun getArrayPropertyStatements(parameter: Parameter): List<String> {
    val statements = mutableListOf<String>()
    if (parameter.properties !is ArrayProperties) return statements

    parameter.properties.minItems?.let { minItems ->
        if (parameter.required)
            statements.add(Statement.MIN_ITEMS.statement(minItems, parameter.name))
        else
            statements.add(Statement.MIN_ITEMS_NULLABLE.statement(minItems, parameter.name))
    }

    parameter.properties.maxItems?.let { maxItems ->
        if (parameter.required)
            statements.add(Statement.MAX_ITEMS.statement(maxItems, parameter.name))
        else
            statements.add(Statement.MAX_ITEMS_NULLABLE.statement(maxItems, parameter.name))
    }

    parameter.properties.uniqueItems.let { uniqueItems ->
        if (uniqueItems) {
            if (parameter.required)
                statements.add(Statement.UNIQUE_ITEMS.statement(uniqueItems, parameter.name))
            else
                statements.add(Statement.UNIQUE_ITEMS_NULLABLE.statement(uniqueItems, parameter.name))
        }
    }

    return statements
}
