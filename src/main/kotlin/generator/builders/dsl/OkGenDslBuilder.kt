package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import generator.builders.routing.routes.PATHSFILE
import generator.capitalize
import generator.decapitalize
import generator.model.GenParameter
import generator.model.Packages
import generator.model.Visibility
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("OkGenDslBuilder.kt")

private const val INNERCLASS = "OKGenRoute"
private const val OUTERCLASS = "OkGenDsl"
private const val APIOPERATIONS = "ApiOperations"
private const val KTORROUTE = "ktorRoute"

fun buildOkGenDsl(dslOperations: List<DSLOperation>, componentNames: List<String>, basePath: String) {
    logger.info("Generating $OUTERCLASS file")

    val route = GenParameter(
        KTORROUTE,
        Route::class.java.asTypeName(),
        Visibility.PRIVATE
    )

    val fileSpec =
        FileSpec.builder(Packages.DSL, OUTERCLASS)
            .addType(
                //Build outer class
                TypeSpec.classBuilder(OUTERCLASS)
                    .getConstructor(listOf(route))
                    .addProperty(
                        PropertySpec.builder(
                            "route",
                            ClassName(
                                Packages.DSL, "$OUTERCLASS.$INNERCLASS"
                            )
                        )
                            .initializer("$INNERCLASS()")
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder(
                            APIOPERATIONS.decapitalize(),
                            ClassName(
                                Packages.DSL,
                                APIOPERATIONS
                            )
                        )
                            .initializer("$APIOPERATIONS()")
                            .addModifiers(KModifier.PRIVATE)
                            .build()
                    )
                    //Build inner Class
                    .addType(
                        getInnerClass(dslOperations)
                    )
                    .build()
            )
            .addImports(componentNames)

    writeFile(fileSpec.build(), basePath)
}

fun getInnerClass(dslOperations: List<DSLOperation>): TypeSpec =
    TypeSpec.classBuilder(INNERCLASS).apply {
        addModifiers(KModifier.INNER)
        getOperationFunctions(dslOperations).forEach {
            addFunction(it)
        }
    }.build()

fun getOperationFunctions(dslOperations: List<DSLOperation>): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()

    dslOperations.map {
        //Define suspend function for operation parameter
        val suspFunc = LambdaTypeName.get(
            receiver = ClassName(Packages.DSLOPERATIONS, it.name.capitalize()),
            returnType = UNIT
        ).copy(suspending = true)

        functions.add(
            FunSpec.builder(it.name)
                .addParameter("function", suspFunc)
                //TODO get the class from Path file
                .addCode(
                    """
                    ${APIOPERATIONS.decapitalize()}.addOperation("${it.name}")
                    $KTORROUTE.${it.method.value}<$PATHSFILE.${it.name.capitalize()}>{
                        val body = call.receive<Pet>()
                        function(${it.name.capitalize()}(body, call))
                    }
                """.trimIndent()
                )
                .build()
        )
    }

    return functions
}

private fun FileSpec.Builder.addImports(componentNames: List<String>): FileSpec.Builder {
    this.addImport("io.ktor.server.resources", "post")
        .addImport("io.ktor.server.resources", "put")
        .addImport("io.ktor.server.resources", "get")
        .addImport("io.ktor.server.resources", "delete")
        .addImport("io.ktor.server.application", "call")
        .addImport("io.ktor.server.request", "receive")
        .addImport(Packages.ROUTES, PATHSFILE)

    componentNames.forEach {
        this.addImport(Packages.MODEL, it)
    }

    return this
}
