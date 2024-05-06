package output

import com.squareup.kotlinpoet.FileSpec
import org.slf4j.LoggerFactory
import java.nio.file.Paths

private val logger = LoggerFactory.getLogger("FileBuilder.kt")

fun writeFile(fileSpec: FileSpec, dir: String) {
    logger.info("Writing file: ${fileSpec.relativePath}")
    fileSpec.writeTo(Paths.get(dir).toFile())
}

