package output

import generator.model.Directories
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("DirectoryBuilder.kt")

fun buildDirectory(destinationPath: String) {
    val directories = Directories(destinationPath)

    val genDir = File(directories.gen)

    if (genDir.exists()) {
        val deleteResult = genDir.deleteRecursively()
        if (!deleteResult) {
            throw Exception("Unable to delete the directory.")
        }
    }

    generateDirectory(directories.gen)
    generateDirectory(directories.dsl)
    generateDirectory(directories.routing)
    generateDirectory(directories.model)
    generateDirectory(directories.plugins)
    generateDirectory(directories.routes)
    generateDirectory(directories.utils)
}

private fun generateDirectory(directoryPath: String) {
    val directory = File(directoryPath)
    logger.info("Creating directory: ${directory.path}")
    if (!directory.mkdirs()) throw Exception("Unable to create the directory.")
}