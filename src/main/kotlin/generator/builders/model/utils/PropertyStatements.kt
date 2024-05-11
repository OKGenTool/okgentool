package generator.builders.model.utils

import datamodel.ComponentProperties
import generator.model.Statement

fun getIntegerPropertyStatements(componentProperty: ComponentProperties): List<String> {
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