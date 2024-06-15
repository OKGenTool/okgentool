package generator.builders

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import generator.model.Parameter

fun TypeSpec.Builder.buildConstructor(parameters: List<Parameter?>): TypeSpec.Builder {
    val constructor = FunSpec.constructorBuilder()
    val properties: MutableList<PropertySpec> = mutableListOf()

    parameters.map {
        if (it != null) {
            constructor.addParameter(it.name, it.type)
            properties.add(
                PropertySpec.builder(it.name, it.type, it.visibility.modifier)
                    .initializer(it.name)
                    .build()
            )
        }
    }

    return this.primaryConstructor(
        constructor.build()
    ).addProperties(properties)
}
