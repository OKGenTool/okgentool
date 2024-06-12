package generator.builders.defaultRouting.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import datamodel.DataType
import datamodel.Parameter
import datamodel.Schema
import datamodel.StringProperties
import generator.capitalize
import generator.model.Packages

fun createExampleObject(schema: Schema): PropertySpec? {
    if (
        schema.parameters.isEmpty() ||
        schema.parameters.any { it.example.toString().isBlank() && it.required }
    ) {
        return null
    }

    return PropertySpec.builder(
        "example" + schema.simplifiedName.capitalize(),
        ClassName(Packages.MODEL, schema.simplifiedName.capitalize())
    )
        .initializer(getInitializerBlock(schema))
        .build()
}

fun getInitializerBlock(schema: Schema): CodeBlock {
    val codeBlock = CodeBlock.builder()
    codeBlock.add("%T(\n", ClassName(Packages.MODEL, schema.simplifiedName.capitalize()))

    schema.parameters.forEach { param ->
        codeBlock.add("\t%L = ", param.name)

        if (param.example.toString().isBlank()) {
            codeBlock.add("null,\n")
            return@forEach
        }

        when (param.dataType) {
            DataType.STRING -> getStringExample(codeBlock, param.example.toString(), param, schema.simplifiedName)

            DataType.INTEGER -> codeBlock.add("%L,\n", param.example)

            DataType.BOOLEAN -> codeBlock.add("%L,\n", param.example)

            DataType.ARRAY -> codeBlock.add("%L,\n", getArrayExample(param.example.toString()))

            DataType.OBJECT -> codeBlock.add("%S,\n", param.example) //TODO()

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

fun getNeededImports(schema: Schema): List<Pair<String, String>> {
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

private fun getStringExample(codeBlock: CodeBlock.Builder, example: String, param: Parameter, simplifiedName: String): CodeBlock.Builder {
    return if (param.properties is StringProperties && param.properties.isEnum && param.example.toString().isNotBlank()) {
        codeBlock.add("%L,\n", simplifiedName.capitalize() + param.name.capitalize() + "." + example)
    } else {
        codeBlock.add("%S,\n", example)
    }
}

private fun getArrayExample(example: String): String {
    return example.replace("[", "listOf(").replace("]", ")")
}
