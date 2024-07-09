package cli

import datamodel.CliModel
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

const val VERSION = "beta"

var packageName: String = ""

fun getClient(args: Array<String>): CliModel {
    var sourcePath = ""
    var destinationPath = ""
    var serverDestinationPath = ""
    var clientDestinationPath = ""

    if (args.isEmpty()) {
        println(getErrorMessage("No arguments provided"))
        exitProcess(1)
    }

    if (args.size == 1 && args[0] == "-h") {
        println(getHelpText())
        exitProcess(1)
    }

    for (i in args.indices) {
        when (args[i]) {
            "-s" -> sourcePath = args[i + 1]
            "-t" -> destinationPath = args[i + 1]
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
    checkDestinationPath(destinationPath)
    checkDestinationPath(serverDestinationPath)
    checkDestinationPath(clientDestinationPath)
    packageName = formatPackageName(packageName)

    if (destinationPath.isEmpty() && serverDestinationPath.isEmpty() && clientDestinationPath.isEmpty()) {
        println(getErrorMessage("Destination Path not provided"))
        exitProcess(1)
    }

    return getCli(
        sourcePath,
        destinationPath,
        serverDestinationPath,
        clientDestinationPath
    )
}

fun getCli(
    sourcePath: String,
    destinationPath: String,
    serverDestinationPath: String,
    clientDestinationPath: String
): CliModel {
    if (destinationPath.isNotEmpty()) {
        return CliModel(
            sourcePath = sourcePath,
            serverDestinationPath = destinationPath,
            clientDestinationPath = destinationPath,
            isServer = true,
            isClient = true
        )
    }

    return CliModel(
        sourcePath = sourcePath,
        serverDestinationPath = serverDestinationPath,
        clientDestinationPath = clientDestinationPath,
        isClient = clientDestinationPath.isNotEmpty(),
        isServer = serverDestinationPath.isNotEmpty()
    )
}

private fun getHelpText(): String {
    return """
        
                       ==/ OKGenToll \==
                          Version: $VERSION
        
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
    if (serverDestinationPath.isEmpty()) {
        return
    }
    val path = Paths.get(serverDestinationPath)
//    if (!Files.exists(path)) {
//        println(getErrorMessage("Directory $serverDestinationPath does not exist"))
//        exitProcess(1)
//    }
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
