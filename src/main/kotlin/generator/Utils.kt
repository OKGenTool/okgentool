package generator

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


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
