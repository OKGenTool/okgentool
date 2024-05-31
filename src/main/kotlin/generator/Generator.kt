package generator

import cli.serverDestinationPath
import datamodel.DataModel
import generator.builders.dsl.buildDSLOperations
import generator.builders.model.buildModel
import generator.builders.routing.routes.buildPaths
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(private val dataModel: DataModel) {
    fun build() = run {
        logger.info("Clean up gen files")
        cleanUp(serverDestinationPath)

        logger.info("Build model files")
        buildModel(dataModel.components)

        logger.info("Build Paths file")
        buildPaths(dataModel.dslOperations)

        logger.info("Build DSL Files")
        buildDSLOperations(
            dataModel.dslOperations,
            dataModel.components.map { it.simplifiedName }
        )
    }
}