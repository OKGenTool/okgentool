package generator.builders.model.utils

import com.squareup.kotlinpoet.*
import datamodel.Component
import generator.model.Packages
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun createDataClassComponent(component: Component, components: List<Component>): FileSpec {
    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(getDataClassBuilder(component, components))
        .addTypes(getEnumBuilders(component))
        .build()
}

fun getDataClassBuilder(
    component: Component,
    components: List<Component>,
    superclassName: ClassName? = null,
    companionObject: TypeSpec? = null,
): TypeSpec {
    val properties = mutableListOf<PropertySpec>()
    val parameters = mutableListOf<ParameterSpec>()

    for (property in component.parameters) {
        val propertyType = getPropertyType(property, component.simplifiedName, components).copy(nullable = true)
        val initializer = if (property.required) property.name else "null"

        val propertySpec = PropertySpec.builder(property.name, propertyType)
            .initializer(property.name)
            .build()

        val parameterSpec = ParameterSpec.builder(property.name, propertyType)
            .defaultValue(initializer)
            .build()

        properties.add(propertySpec)
        parameters.add(parameterSpec)
    }

    val primaryConstructor = FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()

    val dataClassBuilder = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(primaryConstructor)
        .addProperties(properties)
        .addAnnotation(Serializable::class)

    val initCodeBlock = getInitCodeBlock(component)
    if (initCodeBlock.isNotEmpty()) {
        dataClassBuilder.addInitializerBlock(initCodeBlock)
    }

    if (superclassName != null) {
        dataClassBuilder
            .superclass(superclassName)
            .addAnnotation(AnnotationSpec.builder(SerialName::class)
                .addMember("%S", component.simplifiedName).build())
    }

    if (companionObject != null) {
        dataClassBuilder.addType(companionObject)
    }

    return dataClassBuilder.build()
}