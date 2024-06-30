package generator

import cli.serverDestinationPath
import datamodel.DataModel
import generator.builders.defaultRouting.buildDefaultRouting
import generator.builders.dsl.buildDSL
import generator.builders.model.buildModel
import generator.builders.routing.plugins.buildSerialization
import generator.builders.routing.routes.buildPaths
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(private val dataModel: DataModel) {
    fun build() = run {
        logger.info("Clean up gen files")
        cleanUp(serverDestinationPath)

        logger.info("Build model files")
        buildModel(dataModel.schemas)

        logger.info("Build Paths file")
        buildPaths(dataModel.dslOperations)

        logger.info("Build DSL Files")
        buildDSL(
            dataModel.dslOperations,
            dataModel.schemas.map { it.simplifiedName }
        )

        logger.info("Build Serialization File")
        buildSerialization()

        logger.info("Build default routing files")
        buildDefaultRouting(dataModel.dslOperations, dataModel.schemas)
    }
}