package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import generator.capitalize
import generator.model.Parameter
import generator.nullable
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("OperationsParamsBuilder.kt")

fun buildOperationsParams(operationName: String, params: List<Parameter>): List<TypeSpec> {
    val typeSpecs: MutableList<TypeSpec> = mutableListOf()

    params.map {
        if (!it.enum.isNullOrEmpty())
            typeSpecs.add(buildEnumClass(it))
    }

    return typeSpecs
}

private fun buildEnumClass(param: Parameter): TypeSpec {
    val enumClassName = "${param.name.capitalize()}Param"
    val enumClass = TypeSpec.enumBuilder(enumClassName)
        .addModifiers(KModifier.PUBLIC)

    param.enum?.let {
        it.map {
            enumClass.addEnumConstant(it)
        }
    }

    enumClass.addType(
        TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder("fromString")
                    .addParameter("value", String::class.asTypeName().nullable())
                    .returns(param.type)
                    .addCode(
                        """
                        |if (value.isNullOrEmpty()) return null
                        |
                        |val list = value.split(",")
                        |    .mapNotNull {
                        |        try {
                        |            valueOf(it.trim())
                        |        } catch (ex: IllegalArgumentException) {
                        |            null
                        |        }
                        |    }
                        |return list.ifEmpty { null }
                        """.trimMargin()
                    ).build()
            ).build()
    )

    return enumClass.build()
}