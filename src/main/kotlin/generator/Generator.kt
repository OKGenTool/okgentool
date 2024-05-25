package generator

import datamodel.DataModel
import generator.builders.dsl.buildDSLOperations
import generator.builders.model.buildModel
import generator.builders.routing.routes.buildPaths
import org.slf4j.LoggerFactory
import output.buildDirectory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(private val dataModel: DataModel, private val destinationPath: String) {
    fun build() = run {
        logger.info("Build directory structure")
        buildDirectory(destinationPath)

        logger.info("Build model files")
        buildModel(dataModel.components, destinationPath)

        logger.info("Build Paths file")
        buildPaths(dataModel.dslOperations, destinationPath)

        logger.info("Build DSL Files")
        buildDSLOperations(dataModel.dslOperations, destinationPath)
    }
}