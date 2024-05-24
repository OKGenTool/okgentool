package generator.builders.dsl

import com.squareup.kotlinpoet.*
import generator.model.GenParameter
import generator.model.Packages
import generator.model.Visibility
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("OkGenDslBuilder.kt")

fun buildOkGenDsl(basePath: String) {
    logger.info("Generating OkGenDsl file")

    val route = GenParameter(
        "ktorRoute",
        Route::class.java.asTypeName(),
        Visibility.PRIVATE
    )

    val fileSpec =
        FileSpec.builder(Packages.DSL, "OkGenDsl")
            .addType(
                TypeSpec.classBuilder("OkGenDsl")
                    .getConstructor(listOf(route))
                    .addProperty(
                        PropertySpec.builder("route", ANY)
                            .initializer("OKGenRoute()")
                            .build()
                    )
                    .addType(
                        TypeSpec.classBuilder("OKGenRoute")
                            .addModifiers(KModifier.INNER)
                            .build()
                    )
                    .build()

            )




    writeFile(fileSpec.build(), basePath)
}

fun getOperationFunctions(): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()


    return functions
}