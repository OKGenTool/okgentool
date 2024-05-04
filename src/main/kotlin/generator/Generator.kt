package generator

import cli.logger
import datamodel.DataModel
import output.buildDirectory
import generator.builders.model.buildModel

class Generator(dataModel: DataModel, destinationPath: String) {
    init {
        logger().info("Build directory structure")
        buildDirectory(destinationPath)

        logger().info("Build model files")
        buildModel(dataModel.components, destinationPath)
    }
}