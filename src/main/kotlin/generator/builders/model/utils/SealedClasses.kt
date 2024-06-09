package generator.builders.model.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import datamodel.Schema
import generator.model.Packages
import kotlinx.serialization.Serializable

fun createSealedClassComponent(schema: Schema, schemas: List<Schema>): FileSpec {
    val sealedClass = TypeSpec.classBuilder(schema.simplifiedName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(Serializable::class)
        .build()

    return FileSpec.builder(Packages.MODEL, schema.simplifiedName)
        .addType(sealedClass)
        .addTypes(getSubclasses(schema, schemas, schema.simplifiedName))
        .build()
}

private fun getSubclasses(superClassSchema: Schema, schemas: List<Schema>, superclassName: String): List<TypeSpec> {
    val subclasses = mutableListOf<TypeSpec>()

    for (component in superClassSchema.superClassChildSchemas) {
        subclasses.add(
            getDataClassBuilder(
                component,
                schemas,
                ClassName(Packages.MODEL, superclassName),
            )
        )
        subclasses.addAll(getEnumBuilders(component))
    }

    return subclasses
}