package generator.builders.model.utils

import com.squareup.kotlinpoet.TypeSpec
import datamodel.DataType
import datamodel.Schema
import datamodel.StringProperties
import generator.capitalize

fun getEnumBuilders(schema: Schema): List<TypeSpec> {
    val enums = schema.parameters.filter { it.properties is StringProperties && it.properties.isEnum }
    val enumBuilders = mutableListOf<TypeSpec>()
    schema.parameters.forEach { parameter ->
        if (parameter.properties is StringProperties && parameter.properties.values.isNotEmpty()) {
            enums.forEach { enum ->
                val stringProperties = enum.properties as StringProperties
                val enumBuilder = TypeSpec.enumBuilder(schema.simplifiedName + enum.name.capitalize())
                stringProperties.values.forEach { enumValue ->
                    enumBuilder.addEnumConstant(enumValue)
                }
                enumBuilders.add(enumBuilder.build())
            }
        }
    }

    return enumBuilders
}