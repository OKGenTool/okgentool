package generator

import cli.serverDestinationPath
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeName
import generator.model.Packages
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

private val logger = LoggerFactory.getLogger("Utils.kt")

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else
            it.toString()
    }
}

fun String.decapitalize(): String {
    return this.replaceFirstChar {
        it.lowercase(Locale.getDefault())
    }
}

/**
 * Use on class names like: gen.routing.model.Pet
 */
fun getVarNameFromParam(fullClassName: String): String =
    fullClassName
        .split(".")
        .last()
        .decapitalize()

fun <T : Any> convertStringToType(value: String, clazz: KClass<T>): T? {
    return when (clazz) {
        String::class -> value as T
        Int::class -> value.toIntOrNull() as T?
        Double::class -> value.toDoubleOrNull() as T?
        Boolean::class -> value.toBoolean() as T
        Float::class -> value.toFloatOrNull() as T?
        Long::class -> value.toLongOrNull() as T?
        Short::class -> value.toShortOrNull() as T?
        Byte::class -> value.toByteOrNull() as T?
        Char::class -> if (value.length == 1) value[0] as T else null
        else -> null
    }
}

fun cleanUp(path: String) {
    val genFolder = Packages.BASE.replace(".", File.separator)
    val genDir = File("$path${File.separator}$genFolder")

    if (genDir.exists()) {
        val deleteResult = genDir.deleteRecursively()
        if (!deleteResult) {
            throw Exception("Unable to delete the directory.")
        }
    }
}

fun writeFile(fileSpec: FileSpec) {
    logger.info("Writing file: ${fileSpec.relativePath}")
    fileSpec.toBuilder()
        .addFileComment(
            """
            Generated file by OKGenTool

            ### DO NOT EDIT THIS FILE MANUALLY ###
            
            For more information visit: https://github.com/OKGenTool/okgentool
            
        """.trimIndent(),
        )
        .build()
        .writeTo(Paths.get(serverDestinationPath).toFile())
}

fun TypeName.nullable(): TypeName = this.copy(nullable = true)