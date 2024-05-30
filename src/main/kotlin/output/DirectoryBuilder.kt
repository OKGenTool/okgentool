package output

import generator.model.Packages
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("DirectoryBuilder.kt")

public fun cleanUp(path: String) {
    val genFolder = Packages.BASE.replace(".",File.separator)
    val genDir = File("$path${File.separator}$genFolder")

    if (genDir.exists()) {
        val deleteResult = genDir.deleteRecursively()
        if (!deleteResult) {
            throw Exception("Unable to delete the directory.")
        }
    }
}