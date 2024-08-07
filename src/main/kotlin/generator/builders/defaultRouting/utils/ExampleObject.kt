package generator.builders.defaultRouting.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import datamodel.ArrayProperties
import datamodel.DataType
import datamodel.Parameter
import datamodel.Schema
import datamodel.StringProperties
import generator.capitalize
import generator.model.Packages
import generator.nullable

fun getExampleObject(schema: Schema): List<PropertySpec> {
    if (
        schema.superClassChildSchemas.isEmpty() &&
        (schema.parameters.isEmpty() ||
        schema.parameters.any { it.example.toString().isBlank() && it.required })
    ) {
        return listOf(PropertySpec.builder(
            "example" + schema.simplifiedName.capitalize(),
            ClassName(Packages.MODEL, schema.simplifiedName.capitalize()).nullable()
        )
            .initializer("null")
            .build()
        )
    }

    if (schema.superClassChildSchemas.isNotEmpty()) {
        val propsList = mutableListOf<PropertySpec>()

        for (childSchema in schema.superClassChildSchemas) {
            propsList.add(getExampleObject(childSchema).first())
        }

        propsList.add(createSealedExampleObject(schema))

        return propsList
    }

    return listOf(PropertySpec.builder(
        "example" + schema.simplifiedName.capitalize(),
        ClassName(Packages.MODEL, schema.simplifiedName.capitalize())
    )
        .initializer(getInitializerBlock(schema))
        .build()
    )
}

fun getInitializerBlock(schema: Schema): CodeBlock {
    val codeBlock = CodeBlock.builder()
    codeBlock.add("%T(\n", ClassName(Packages.MODEL, schema.simplifiedName.capitalize()))

    schema.parameters.forEach { param ->
        codeBlock.add("\t%L = ", param.name)

        if (
            param.example.toString().isBlank() &&
            !((param.dataType == DataType.OBJECT) ||
                    (param.properties is ArrayProperties && param.properties.arrayItemsDataType == DataType.OBJECT))
        ) {
            codeBlock.add("null,\n")
            return@forEach
        }

        when (param.dataType) {
            DataType.STRING -> getStringExample(codeBlock, param.example.toString(), param, schema.simplifiedName)

            DataType.INTEGER -> codeBlock.add("%L,\n", param.example)

            DataType.BOOLEAN -> codeBlock.add("%L,\n", param.example)

            DataType.ARRAY -> codeBlock.add("%L,\n", getArrayExample(param.example.toString(), param))

            DataType.OBJECT -> codeBlock.add("%L,\n", "example" + param.name.capitalize())

            DataType.LONG -> codeBlock.add("%L,\n", param.example)

            DataType.FLOAT -> codeBlock.add("%Lf,\n", param.example.toString().toFloat())

            DataType.DOUBLE -> codeBlock.add("%L,\n", param.example.toString().toDouble())

            DataType.BYTE -> codeBlock.add("%L,\n", param.example) //TODO()

            DataType.BINARY -> codeBlock.add("%S,\n", param.example) //TODO()

            DataType.DATE -> codeBlock.add("LocalDate.parse(%S, localDateFormatter),\n", param.example)

            DataType.DATE_TIME -> codeBlock.add("LocalDateTime.parse(%S, dateTimeFormatter),\n", param.example)

            DataType.PASSWORD -> codeBlock.add("%S,\n", param.example)

            DataType.INT -> codeBlock.add("%L,\n", param.example)

            DataType.NUMBER -> codeBlock.add("%L,\n", param.example.toString().toDouble())
        }
    }
    codeBlock.add(")")

    return codeBlock.build()
}

fun getExampleImports(schema: Schema): List<Pair<String, String>> {
    val imports = mutableListOf<Pair<String, String>>()
    schema.parameters.forEach { param ->
        if (
            param.dataType == DataType.STRING &&
            param.properties is StringProperties &&
            param.properties.isEnum &&
            param.example.toString().isNotBlank()
        ) {
            imports.add(Packages.MODEL to schema.simplifiedName.capitalize() + param.name.capitalize())
        }
    }
    return imports
}

private fun createSealedExampleObject(schema: Schema): PropertySpec {
    val validChildExample = schema.superClassChildSchemas
        .firstOrNull { it.parameters.isNotEmpty() && it.parameters.all { param -> param.example.toString().isNotBlank() || !param.required } }

    if (validChildExample == null) {
        return PropertySpec.builder(
            "example" + schema.simplifiedName.capitalize(),
            ClassName(Packages.MODEL, schema.simplifiedName.capitalize()).nullable()
        )
            .initializer("null")
            .build()
    }

    return PropertySpec.builder(
            "example" + schema.simplifiedName.capitalize(),
            ClassName(Packages.MODEL, schema.simplifiedName.capitalize())
        )
            .initializer("example${validChildExample.simplifiedName.capitalize()}")
            .build()

}

private fun getStringExample(codeBlock: CodeBlock.Builder, example: String, param: Parameter, simplifiedName: String): CodeBlock.Builder {
    return if (param.properties is StringProperties && param.properties.isEnum && param.example.toString().isNotBlank()) {
        codeBlock.add("%L,\n", simplifiedName.capitalize() + param.name.capitalize() + "." + example)
    } else {
        codeBlock.add("%S,\n", example)
    }
}

private fun getArrayExample(example: String, param: Parameter): String {
    if (param.properties is ArrayProperties && param.properties.arrayItemsDataType == DataType.OBJECT) {
        return "listOf(example${param.properties.arrayItemsSchemaName?.split('/')?.last()?.capitalize()})"
    }
    return example.replace("[", "listOf(").replace("]", ")")
}
