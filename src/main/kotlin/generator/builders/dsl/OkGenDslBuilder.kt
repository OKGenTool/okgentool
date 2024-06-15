package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.*
import generator.builders.buildConstructor
import generator.builders.routing.routes.PATHSFILE
import generator.capitalize
import generator.decapitalize
import generator.model.Imports.*
import generator.model.Imports.Companion.addCustomImport
import generator.model.Packages
import generator.model.Parameter
import generator.model.Visibility
import generator.writeFile
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("OkGenDslBuilder.kt")

private const val INNERCLASS = "OKGenRoute"
private const val OUTERCLASS = "OkGenDsl"
private const val APIOPERATIONS = "ApiOperations"
private const val KTORROUTE = "ktorRoute"

fun buildOkGenDsl(dslOperations: List<DSLOperation>, componentNames: List<String>, parameters: List<Parameter>) {
    logger.info("Generating $OUTERCLASS file")

    val logger = PropertySpec
        .builder("logger", ClassName("org.slf4j", "Logger"))
        .initializer(
            "%T.getLogger(%T::class.java.simpleName)",
            ClassName("org.slf4j", "LoggerFactory"),
            ClassName(Packages.DSL, "OkGenDsl")
        ).addModifiers(KModifier.PRIVATE)
        .build()

    val route = Parameter(
        KTORROUTE,
        Route::class.java.asTypeName(),
        Visibility.PRIVATE
    )

    val fileSpec =
        FileSpec.builder(Packages.DSL, OUTERCLASS)
            .addProperty(logger)
            .addType(
                //Build outer class
                TypeSpec.classBuilder(OUTERCLASS)
                    .buildConstructor(listOf(route))
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
                    .addType(buildInnerClass(dslOperations))
                    .build()
            )
            .addImports(componentNames, parameters)

    writeFile(fileSpec.build())
}

private fun buildInnerClass(dslOperations: List<DSLOperation>): TypeSpec =
    TypeSpec.classBuilder(INNERCLASS).apply {
        addModifiers(KModifier.INNER)
        buildOperationFunctions(dslOperations).forEach {
            addFunction(it)
        }
    }.build()

private fun buildOperationFunctions(dslOperations: List<DSLOperation>): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()

    dslOperations.map {
        if (it.name in notImplemented) return@map

        //Define suspend function for operation parameter
        val suspFunc = LambdaTypeName.get(
            receiver = ClassName(Packages.DSLOPERATIONS, it.name.capitalize()),
            returnType = UNIT
        ).copy(suspending = true)


        val codeBlock = CodeBlock.builder()
            .add("${APIOPERATIONS.decapitalize()}.addOperation(\"${it.name}\")\n")
            .add("$KTORROUTE.${it.method.value}<$PATHSFILE.${it.name.capitalize()}>{\n")
            .getRequestCode(it)
            .add("}")
            .build()

        val commentBlock = CodeBlock.builder()
            .add("Summary: ${it.summary}\n\n")
            .add("Description: ${it.description}")
            .build()

        functions.add(
            FunSpec.builder(it.name)
                .addKdoc(commentBlock)
                .addParameter("function", suspFunc)
                .addCode(codeBlock)
                .build()
        )
    }

    return functions
}

private fun CodeBlock.Builder.getRequestCode(operation: DSLOperation): CodeBlock.Builder {
    var parameters = ""

    //For Requests with query parameters
    if (!operation.parameters.isNullOrEmpty()) {
        operation.parameters.map { parameter ->
            when (parameter) {
                is PathParameter -> {
                    this.add("\tval ${parameter.name} = call.parameters[\"${parameter.name}\"]${getConvertion(parameter.type)}\n")
                }

                is QueryParameterSingle -> {
                    this.add("\tval ${parameter.name} = call.request.rawQueryParameters[\"${parameter.name}\"]\n")
                }

                is QueryParameterArray -> {
                    this.add("\tval ${parameter.name} = call.request.rawQueryParameters[\"${parameter.name}\"]\n")
                        .add("\t\t\t?.split(\",\")\n")
                }

                is QueryParameterEnum -> {
                    this.add("\tval ${parameter.name} = ${parameter.name.capitalize()}Param.fromString(\n")
                        .add("\t\tcall.request.rawQueryParameters[\"${parameter.name}\"]\n")
                        .add("\t)\n")
                }

                is HeaderParameter -> {
                    this.add("\tval ${parameter.name} = call.request.header(\"${parameter.name}\")\n")
                }
            }

            parameters += "${parameter.name},"
        }
    }

    //For requests with body
    if (operation.requestBody != null) {
        val body = operation.requestBody
        var className: String = ""

        when (body) {
            is BodyObj -> className = body.dataType.kotlinType.simpleName
            is BodyRef -> className = SchemaProps.getRefSimpleName(body.schemaRef)
            is BodyCollRef -> className = body.className
            is BodyCollPojo -> className = body.dataType.kotlinType.simpleName
        }

        this.add("\tvar body:${className.capitalize()}? = null\n")
            .add("\ttry {\n")
            .add("\t\tbody = call.receive<${className.capitalize()}>()\n")
            .add("\t}catch (ex: Exception){\n")
            .add("\t\tlogger.error(ex.message)\n")
            .add("\t}\n")
        parameters = "body, $parameters"
    }

    this.add("\tfunction(${operation.name.capitalize()}($parameters call))")
    return this
}

fun getConvertion(type: DataType): String =
    when (type) {
        DataType.INTEGER -> "?.toIntOrNull()"
        DataType.LONG -> "?.toLongOrNull()"
        DataType.FLOAT -> "?.toFloatOrNull()"
        DataType.DOUBLE -> "?.toDoubleOrNull()"
        DataType.BYTE -> "?.toByteOrNull()"
        DataType.BOOLEAN -> "?.toBoolean()"
        DataType.NUMBER -> "?.toDoubleOrNull()"
        DataType.ARRAY -> "?.split(\",\")"
        else -> {
            ""
        }
    }

private fun FileSpec.Builder.addImports(componentNames: List<String>, parameters: List<Parameter>): FileSpec.Builder {
    this
        .addCustomImport(KTOR_SERVER_POST)
        .addCustomImport(KTOR_SERVER_PUT)
        .addCustomImport(KTOR_SERVER_GET)
        .addCustomImport(KTOR_SERVER_DELETE)
        .addCustomImport(KTOR_APPLICATION_CALL)
        .addCustomImport(KTOR_SERVER_RECEIVE)
        .addCustomImport(KTOR_SERVER_HEADER)
        .addImport(Packages.ROUTES, PATHSFILE)

    componentNames.forEach {
        this.addImport(Packages.MODEL, it)
    }

    parameters.forEach {
        this.addImport(Packages.DSLOPERATIONS, "${it.name.capitalize()}Param")
    }

    return this
}

//TODO implement these operations
val notImplemented = setOf(
    "createUsersWithListInput",
    "getInventory", "updatePetWithForm",
)