package generator.builders.model.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import datamodel.*
import generator.capitalize
import generator.model.Packages
import generator.nullable

fun getParameterType(parameter: Parameter, componentName: String, schemas: List<Schema>): TypeName {
    return when {
        parameter.properties is StringProperties && parameter.properties.isEnum ->
            enumParameterType(parameter, componentName)

        parameter.dataType == DataType.OBJECT -> objectParameterType(parameter, schemas)

        parameter.dataType == DataType.ARRAY -> arrayParameterType(parameter, schemas)

        else -> simplePropertyType(parameter)
    }
}

private fun enumParameterType(parameter: Parameter, componentName: String): TypeName {
    if (parameter.properties is StringProperties && parameter.properties.values.isEmpty())
        throw IllegalArgumentException("Enum property ${parameter.name} must have values")
    return if (parameter.required)
        ClassName(
            Packages.MODEL,
            componentName + parameter.name.capitalize()
        )
    else
        ClassName(
            Packages.MODEL,
            componentName + parameter.name.capitalize()
        ).nullable()
}

private fun objectParameterType(parameter: Parameter, schemas: List<Schema>): TypeName {
    if (parameter.schemaName.isEmpty())
        throw IllegalArgumentException("Object property ${parameter.name} must have schema name")
    val relatedComponent = schemas.find { it.schemaName == parameter.schemaName }
        ?: throw IllegalArgumentException("Object property ${parameter.name} must have schema name")
    return if (parameter.required) ClassName(Packages.MODEL, relatedComponent.simplifiedName)
    else ClassName(Packages.MODEL, relatedComponent.simplifiedName).nullable()
}

private fun arrayParameterType(parameter: Parameter, schemas: List<Schema>): TypeName {
    if (parameter.properties !is ArrayProperties){
        throw IllegalArgumentException("Array property ${parameter.name} must have properties")
    }
    val properties = parameter.properties

    if (properties.arrayProperties != null) {
        return if (parameter.required) parameter.dataType.kotlinType.parameterizedBy(
            arrayChildParameterType(properties.arrayProperties, schemas)
        )
        else parameter.dataType.kotlinType.parameterizedBy(
            arrayChildParameterType(properties.arrayProperties, schemas)
        ).nullable()
    }

    if (properties.arrayItemsDataType != null && properties.arrayItemsDataType != DataType.OBJECT)
        return if (parameter.required) parameter.dataType.kotlinType.parameterizedBy(properties.arrayItemsDataType.kotlinType)
        else parameter.dataType.kotlinType.parameterizedBy(properties.arrayItemsDataType.kotlinType).nullable()

    if (!properties.arrayItemsSchemaName.isNullOrBlank()) {
        val relatedComponent = schemas.find { it.schemaName == properties.arrayItemsSchemaName }
            ?: throw IllegalArgumentException("Array property ${parameter.name} must have schema name")
        return if (parameter.required)
            parameter.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent.simplifiedName
                )
            )
        else
            parameter.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent.simplifiedName
                )
            ).nullable()
    }

    throw IllegalArgumentException("Array property ${parameter.name} must have items type")
}

fun arrayChildParameterType(arrayProperties: ParameterProperties, schemas: List<Schema>): TypeName {
    if (arrayProperties !is ArrayProperties)
        throw IllegalArgumentException("Array property must have properties")

    if (arrayProperties.arrayProperties != null) {
        return DataType.ARRAY.kotlinType.parameterizedBy(
            arrayChildParameterType(arrayProperties.arrayProperties, schemas)
        )
    }

    if (arrayProperties.arrayItemsDataType != null && arrayProperties.arrayItemsDataType != DataType.OBJECT)
        return  DataType.ARRAY.kotlinType.parameterizedBy(
            arrayProperties.arrayItemsDataType.kotlinType
        )

    if (!arrayProperties.arrayItemsSchemaName.isNullOrBlank()) {
        val relatedComponent = schemas.find { it.schemaName == arrayProperties.arrayItemsSchemaName }
            ?: throw IllegalArgumentException("Array property must have schema name")
        return DataType.ARRAY.kotlinType.parameterizedBy(ClassName(
            Packages.MODEL,
            relatedComponent.simplifiedName
            )
        )
    }

    throw IllegalArgumentException("Array property must have items type")
}

private fun simplePropertyType(property: Parameter): TypeName {
    return if (property.required) property.dataType.kotlinType
    else property.dataType.kotlinType.nullable()
}
