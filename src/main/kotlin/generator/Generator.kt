package generator

import datamodel.DataModel
import generator.builders.model.buildModel
import org.slf4j.LoggerFactory
import output.buildDirectory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(dataModel: DataModel, destinationPath: String) {
    init {
        logger.info("Build directory structure")
        buildDirectory(destinationPath)

        logger.info("Build model files")
        buildModel(dataModel.components, destinationPath)
    }
}