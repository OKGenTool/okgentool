package generator

import cli.logger
import output.buildDirectory
import generator.builders.buildModel

class Generator(destinationPath: String) {
    init {
        logger().info("Build directory structure")
        buildDirectory(destinationPath)

        logger().info("Build model files")
        buildModel(destinationPath)
    }
}