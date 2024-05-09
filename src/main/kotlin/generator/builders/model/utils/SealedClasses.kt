package generator.builders.model.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import datamodel.Component
import generator.model.Packages
import kotlinx.serialization.Serializable

fun createSealedClassComponent(component: Component, components: List<Component>): FileSpec {
    val sealedClass = TypeSpec.classBuilder(component.simplifiedName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(Serializable::class)
        .build()

    return FileSpec.builder(Packages.MODEL, component.simplifiedName)
        .addType(sealedClass)
        .addTypes(getSubclasses(component, components, component.simplifiedName))
        .build()
}

private fun getSubclasses(superClassComponent: Component, components: List<Component>, superclassName: String): List<TypeSpec> {
    val subclasses = mutableListOf<TypeSpec>()

    for (component in superClassComponent.superClassChildComponents) {
        subclasses.add(
            getDataClassBuilder(
                component,
                components,
                ClassName(Packages.MODEL, superclassName),
            )
        )
        subclasses.addAll(getEnumBuilders(component))
    }

    return subclasses
}