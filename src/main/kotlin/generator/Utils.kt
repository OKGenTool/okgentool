package generator

import java.util.*


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
