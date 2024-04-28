package output

import cli.logger
import com.squareup.kotlinpoet.FileSpec
import java.nio.file.Paths


fun writeFile(fileSpec: FileSpec, dir: String) {
    logger().info("Writing file: ${fileSpec.relativePath}")
    fileSpec.writeTo(Paths.get(dir).toFile())
}

