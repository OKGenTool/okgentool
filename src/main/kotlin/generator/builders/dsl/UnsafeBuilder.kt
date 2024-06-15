package generator.builders.dsl

import com.squareup.kotlinpoet.*
import generator.model.Imports.*
import generator.model.Imports.Companion.addCustomImport
import generator.model.Imports.KTOR_HTTP_STATUS_CODE
import generator.model.Packages
import generator.writeFile
import io.ktor.http.*
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("UnsafeBuilder.kt")

fun buildUnsafe() {
    // Class definition
    val className = "Unsafe"
    val callParameter = ParameterSpec.builder("call", ClassName("io.ktor.server.application", "ApplicationCall"))
        .build()
    val classSpec = TypeSpec.classBuilder(className)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(callParameter)
                .build()
        )
        .addProperty(
            PropertySpec.builder("call", ClassName("io.ktor.server.application", "ApplicationCall"))
                .initializer("call")
                .addModifiers(KModifier.PRIVATE)
                .build()
        )
        .addFunction(buildUnsafeFunction("NotImplemented"))
        .addFunction(buildUnsafeFunction("InternalServerError"))
        .addFunction(buildUnsafeFunction("OK"))
        .addFunction(buildUnsafeFunction("NotFound"))
        .addFunction(buildUnsafeFunction("BadRequest"))
        .build()

    // File specification
    val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, className)
        .addCustomImport(KTOR_SERVER_RESPOND)
        .addCustomImport(KTOR_APPLICATION_APPLICATIONCALL)
        .addCustomImport(KTOR_HTTP_STATUS_CODE)
        .addType(classSpec)
        .build()

    // Write the file
    writeFile(fileSpec)
}

fun buildUnsafeFunction(name: String): FunSpec {
    return FunSpec.builder("respond$name")
        .addModifiers(KModifier.SUSPEND)
        .addParameter(
            ParameterSpec.builder("description", STRING)
                .defaultValue("%T.$name.description", HttpStatusCode.javaClass.asTypeName())
                .build()
        )
        .addStatement("call.respond(HttpStatusCode.$name.description(description))")
        .build()
}