package generator

import java.util.*


fun String.capitalize(): String{
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else
            it.toString()
    }
}

