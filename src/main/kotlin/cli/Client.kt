package cli

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

data class Client(
    val sourcePath: String,
    val serverDestinationPath: String,
    val clientDestinationPath: String,
    val packageName: String,
)

fun getClient(args: Array<String>): Client {
    if (args.isEmpty()) {
        println(getErrorMessage("No arguments provided"))
        exitProcess(1)
    }

    if (args.size == 1 && args[0] == "-h") {
        println(getHelpText())
        exitProcess(1)
    }

    var sourcePath = ""
    var serverDestinationPath = ""
    var clientDestinationPath = ""
    var packageName = ""

    for (i in args.indices) {
        when (args[i]) {
            "-s" -> sourcePath = args[i + 1]
            "-ts" -> serverDestinationPath = args[i + 1]
            "-tc" -> clientDestinationPath = args[i + 1]
            "-p" -> packageName = args[i + 1]
            else -> {}
        }
    }

    if (
        sourcePath.isEmpty() ||
        (serverDestinationPath.isEmpty() && clientDestinationPath.isEmpty())
        ) {
        println(getErrorMessage("Invalid command line arguments"))
        exitProcess(1)
    }

    checkSourcePath(sourcePath)
    checkDestinationPath(serverDestinationPath)
    checkDestinationPath(clientDestinationPath)

    return Client(
        sourcePath,
        serverDestinationPath,
        clientDestinationPath,
        formatPackageName(packageName)
    )
}

private fun getHelpText(): String {
    return """
        
                       ==/ OKGenToll \==
                          Version 0.1
        
                       ==/   Usage   \==
        -s <sourcePath> -ts <targetPath> -tc <targetPath>
        
        -s <sourcePath>     Path to the source OpenAPI Description file
        -ts <targetPath>    Path to the target server generated code
        -tc <targetPath>    Path to the target client generated code
        
                       ==/  Options  \==
        -p <packageName>    Package name for the generated code
        -h                  Display this usage guide message
        
    """.trimIndent()
}

private fun getErrorMessage(message: String): String {
    return """
        
                       ==/ OKGenToll \==
        
        $message
        
        Use -h for help
        
    """.trimIndent()
}

private fun checkSourcePath(sourcePath: String) {
    val path = Paths.get(sourcePath)
    if (!Files.exists(path)) {
        println(getErrorMessage("File $sourcePath does not exist"))
        exitProcess(1)
    }
    if (!sourcePath.endsWith(".yaml") && !sourcePath.endsWith(".json")) {
        println(getErrorMessage("Invalid source file format"))
        exitProcess(1)
    }
}

private fun checkDestinationPath(serverDestinationPath: String) {
    val path = Paths.get(serverDestinationPath)
    if (!Files.exists(path)) {
        println(getErrorMessage("Directory $serverDestinationPath does not exist"))
        exitProcess(1)
    }
    if (!Files.isDirectory(path)) {
        println(getErrorMessage("$serverDestinationPath is not a directory"))
        exitProcess(1)
    }
}

private fun formatPackageName(packageName: String): String {
    if (packageName.isEmpty()) {
        return ""
    }
    if (!packageName.endsWith(".")) {
        return "$packageName."
    }
    return packageName
}
