package generator.builders.model.utils

import com.squareup.kotlinpoet.*
import datamodel.Component
import generator.model.Packages
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

    for (property in component.parameters) {
        val propertyType = getPropertyType(property, component.simplifiedName, components)

        val propertySpec = PropertySpec.builder(property.name, propertyType)
            .initializer(property.name)
            .build()
        properties.add(propertySpec)
    }

    val primaryConstructor = FunSpec.constructorBuilder()
        .addParameters(properties.map { ParameterSpec(it.name, it.type) })
        .build()

    val dataClassBuilder = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(primaryConstructor)
        .addProperties(properties)
        .addAnnotation(Serializable::class)

    if (superclassName != null) {
        dataClassBuilder.superclass(superclassName)
    }

    if (companionObject != null) {
        dataClassBuilder.addType(companionObject)
    }

    return dataClassBuilder.build()
}