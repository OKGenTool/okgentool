package generator.builders.model.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import datamodel.Component
import datamodel.ComponentProperty
import datamodel.DataType
import generator.model.Packages

fun getPropertyType(property: ComponentProperty, componentName: String, components: List<Component>): TypeName {
    return when {
        property.isEnum -> enumPropertyType(property, componentName)

        property.dataType == DataType.OBJECT -> objectPropertyType(property, components)

        property.dataType == DataType.ARRAY -> arrayPropertyType(property, components)

        else -> simplePropertyType(property)
    }
}

private fun enumPropertyType(property: ComponentProperty, componentName: String): TypeName {
    if (property.values.isEmpty())
        throw IllegalArgumentException("Enum property ${property.name} must have values")
    return if (property.required)
        ClassName(
            Packages.MODEL,
            componentName + property.name.capitalize()
        )
    else
        ClassName(
            Packages.MODEL,
            componentName + property.name.capitalize()
        ).copy(nullable = true)
}

private fun objectPropertyType(property: ComponentProperty, components: List<Component>): TypeName {
    if (property.schemaName.isEmpty())
        throw IllegalArgumentException("Object property ${property.name} must have schema name")
    val relatedComponent = components.find { it.schemaName == property.schemaName }
        ?: throw IllegalArgumentException("Object property ${property.name} must have schema name")
    return if (property.required) ClassName(Packages.MODEL, relatedComponent.simplifiedName)
    else ClassName(Packages.MODEL, relatedComponent.simplifiedName).copy(nullable = true)
}

private fun arrayPropertyType(property: ComponentProperty, components: List<Component>): TypeName {
    if (property.arrayProperties != null) {
        return property.dataType.kotlinType.parameterizedBy(
            arrayPropertyType(property.arrayProperties, components)
        )
    }

    if (property.arrayItemsType != null && property.arrayItemsType != DataType.OBJECT)
        return if (property.required) property.dataType.kotlinType.parameterizedBy(property.arrayItemsType.kotlinType)
        else property.dataType.kotlinType.parameterizedBy(property.arrayItemsType.kotlinType).copy(nullable = true)

    if (!property.arrayItemsSchemaName.isNullOrBlank()){
        val relatedComponent = components.find { it.schemaName == property.arrayItemsSchemaName }
            ?: throw IllegalArgumentException("Array property ${property.name} must have schema name")
        return if (property.required)
            property.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent.simplifiedName
                )
            )
        else
            property.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent.simplifiedName
                )
                .copy(nullable = true)
            )
    }

    throw IllegalArgumentException("Array property ${property.name} must have items type")
}

private fun simplePropertyType(property: ComponentProperty): TypeName {
    return if (property.required) property.dataType.kotlinType
    else property.dataType.kotlinType.copy(nullable = true)
}
