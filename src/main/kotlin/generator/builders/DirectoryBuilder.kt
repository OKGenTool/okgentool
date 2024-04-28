package generator.builders

import generator.model.Directories
import java.io.File

fun buildDirectory(destinationPath: String): Directories {
    val directories = Directories(
        base = destinationPath,
        gen = "$destinationPath/gen",
        dsl = "$destinationPath/gen/dsl",
        routing = "$destinationPath/gen/routing",
        models = "$destinationPath/gen/routing/models",
        plugins = "$destinationPath/gen/routing/plugins",
        routes = "$destinationPath/gen/routing/routes",
        utils = "$destinationPath/gen/utils"
    )

    val baseDirectory = File(directories.gen)

    if (baseDirectory.exists()) {
        val deleteResult = baseDirectory.deleteRecursively()
        if (!deleteResult) {
            throw Exception("Unable to delete the directory.")
        }
    }

    generateDirectory(directories.gen)
    generateDirectory(directories.dsl)
    generateDirectory(directories.routing)
    generateDirectory(directories.models)
    generateDirectory(directories.plugins)
    generateDirectory(directories.routes)
    generateDirectory(directories.utils)

    return directories
}

private fun generateDirectory(directoryPath: String) {
    val directory = File(directoryPath)
    if (!directory.mkdirs()) throw Exception("Unable to create the directory.")
}