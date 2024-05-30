package output

import generator.model.Packages
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("DirectoryBuilder.kt")

public fun cleanUp(path: String) {
    //TODO may not work in unix based systems
    val genFolder = Packages.BASE.replace(".","\\")
    val genDir = File("$path\\$genFolder")

    if (genDir.exists()) {
        val deleteResult = genDir.deleteRecursively()
        if (!deleteResult) {
            throw Exception("Unable to delete the directory.")
        }
    }
}