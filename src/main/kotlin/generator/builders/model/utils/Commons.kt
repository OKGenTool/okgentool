package generator.builders.model.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.model.Packages

fun getPropertyType(property: ComponentProperties, componentName: String, components: List<Component>): TypeName {
    return when {
        property.isEnum && property.values.isNotEmpty() -> ClassName(
            Packages.MODEL,
            componentName + property.name.capitalize()
        )

        property.schemaName.isNotEmpty() -> {
            val relatedComponent = components.find { it.schemaName == property.schemaName }
            ClassName(Packages.MODEL, relatedComponent!!.simplifiedName)
        }

        (property.dataType == DataType.ARRAY) && (property.arrayItemsType != null) ->
            property.dataType.kotlinType.parameterizedBy(property.arrayItemsType.kotlinType)

        (property.dataType == DataType.ARRAY) && !(property.arrayItemsSchemaName.isNullOrBlank()) -> {
            val relatedComponent = components.find { it.schemaName == property.arrayItemsSchemaName }
            property.dataType.kotlinType.parameterizedBy(
                ClassName(
                    Packages.MODEL,
                    relatedComponent!!.simplifiedName
                )
            )
        }

        else ->
            if (property.required) property.dataType!!.kotlinType
            else property.dataType!!.kotlinType.copy(nullable = true)
    }
}