package generator.builders.model.utils

import com.squareup.kotlinpoet.TypeSpec
import datamodel.Component

fun getEnumBuilders(component: Component): List<TypeSpec> {
    val enums = component.parameters.filter { it.isEnum }
    val enumBuilders = mutableListOf<TypeSpec>()
    component.parameters.forEach { property ->
        if (property.values.isNotEmpty()) {
            enums.forEach { enum ->
                val enumBuilder = TypeSpec.enumBuilder(component.simplifiedName + enum.name.capitalize())
                enum.values.forEach { enumValue ->
                    enumBuilder.addEnumConstant(enumValue)
                }
                enumBuilders.add(enumBuilder.build())
            }
        }
    }

    return enumBuilders
}