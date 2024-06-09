package generator.builders.model.utils

import com.squareup.kotlinpoet.*
import datamodel.Schema
import generator.model.Packages
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun createDataClassComponent(schema: Schema, schemas: List<Schema>): FileSpec {
    return FileSpec.builder(Packages.MODEL, schema.simplifiedName)
        .addType(getDataClassBuilder(schema, schemas))
        .addTypes(getEnumBuilders(schema))
        .build()
}

fun getDataClassBuilder(
    schema: Schema,
    schemas: List<Schema>,
    superclassName: ClassName? = null,
    companionObject: TypeSpec? = null,
): TypeSpec {
    val properties = mutableListOf<PropertySpec>()
    val parameters = mutableListOf<ParameterSpec>()

    for (property in schema.parameters) {
        val parameterType = getParameterType(property, schema.simplifiedName, schemas)

        val propertySpec = PropertySpec.builder(property.name, parameterType)
            .initializer(property.name)
            .build()

        val parameterSpec = if(property.required)
            ParameterSpec.builder(property.name, parameterType)
                .build()
        else
            ParameterSpec.builder(property.name, parameterType)
                .defaultValue("null")
                .build()

        properties.add(propertySpec)
        parameters.add(parameterSpec)
    }

    val primaryConstructor = FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()

    val dataClassBuilder = TypeSpec.classBuilder(schema.simplifiedName)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(primaryConstructor)
        .addProperties(properties)
        .addAnnotation(Serializable::class)

    val initCodeBlock = getInitCodeBlock(schema)
    if (initCodeBlock.isNotEmpty()) {
        dataClassBuilder.addInitializerBlock(initCodeBlock)
    }

    if (superclassName != null) {
        dataClassBuilder
            .superclass(superclassName)
            .addAnnotation(AnnotationSpec.builder(SerialName::class)
                .addMember("%S", schema.simplifiedName).build())
    }

    if (companionObject != null) {
        dataClassBuilder.addType(companionObject)
    }

    return dataClassBuilder.build()
}