package generator

import datamodel.DataModel
import generator.builders.DSL.buildDSLOperations
import generator.builders.model.buildModel
import org.slf4j.LoggerFactory
import output.buildDirectory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(private val dataModel: DataModel, private val destinationPath: String) {
    fun build() = run {
        logger.info("Build directory structure")
        buildDirectory(destinationPath)

        logger.info("Build model files")
        buildModel(dataModel.components, destinationPath)

        logger.info("Build DSL Files")
        buildDSLOperations(dataModel.operations, destinationPath)
    }
}