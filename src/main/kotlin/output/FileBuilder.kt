package output

import cli.serverDestinationPath
import com.squareup.kotlinpoet.FileSpec
import org.slf4j.LoggerFactory
import java.nio.file.Paths

private val logger = LoggerFactory.getLogger("FileBuilder.kt")

fun writeFile(fileSpec: FileSpec) {
    logger.info("Writing file: ${fileSpec.relativePath}")
    fileSpec.writeTo(Paths.get(serverDestinationPath).toFile())
}

