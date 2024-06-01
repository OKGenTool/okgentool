package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.DSLOperation
import datamodel.DataType
import datamodel.In
import generator.builders.routing.routes.PATHSFILE
import generator.capitalize
import generator.decapitalize
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

fun buildOkGenDsl(dslOperations: List<DSLOperation>, componentNames: List<String>) {
    logger.info("Generating $OUTERCLASS file")

    val route = Parameter(
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

    writeFile(fileSpec.build())
}

private fun getInnerClass(dslOperations: List<DSLOperation>): TypeSpec =
    TypeSpec.classBuilder(INNERCLASS).apply {
        addModifiers(KModifier.INNER)
        getOperationFunctions(dslOperations).forEach {
            addFunction(it)
        }
    }.build()

private fun getOperationFunctions(dslOperations: List<DSLOperation>): List<FunSpec> {
    val functions = mutableListOf<FunSpec>()

    dslOperations.map {
        if (it.name in notImplemented) return@map //TODO implement these operations

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

        functions.add(
            FunSpec.builder(it.name)
                .addParameter("function", suspFunc)
                .addCode(codeBlock.build())
                .build()
        )
    }

    return functions
}

private fun CodeBlock.Builder.getRequestCode(operation: DSLOperation): CodeBlock.Builder {
    //For Requests with query parameters
    if (!operation.parameters.isNullOrEmpty()) {
        var parameters = ""
        operation.parameters.map {
            when (it.`in`) {
                In.PATH -> {
                    this.add("\tval ${it.name} = call.parameters[\"${it.name}\"]${getConvertion(it.type)}\n")
                }

                else -> {
                    if (it.type == DataType.ARRAY)
                        this.add("\tval ${it.name} = call.request.rawQueryParameters[\"${it.name}\"]?.split(\",\")\n")
                    else
                        this.add("\tval ${it.name} = call.request.rawQueryParameters[\"${it.name}\"]\n")
                }
            }
            parameters += "${it.name},"
        }
        this.add("\tfunction(${operation.name.capitalize()}($parameters call))")
    } else {
        //For requests with body
        this.add("\tval body = call.receive<Pet>()\n")
            .add("\tfunction(${operation.name.capitalize()}(body, call))")
    }
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

//TODO implement these operations
val notImplemented = setOf(
    "postPetPetIdUploadImage", "getInventory", "placeOrder", "createUser", "createUsersWithListInput",
    "logoutUser", "updateUser", "uploadFile"
)