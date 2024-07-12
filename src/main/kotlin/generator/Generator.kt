package generator

import datamodel.CliModel
import datamodel.DataModel
import generator.builders.client.buildClient
import generator.builders.defaultRouting.buildDefaultRouting
import generator.builders.dsl.buildDSL
import generator.builders.model.buildModel
import generator.builders.routing.plugins.buildSerialization
import generator.builders.routing.routes.buildPaths
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Generator::class.java.simpleName)

class Generator(private val dataModel: DataModel, private val cli: CliModel) {
    fun build() = run {
        logger.info("Clean up gen files")
        cleanUp(cli.serverDestinationPath)
        cleanUp(cli.clientDestinationPath)

        logger.info("Build model files")
        if (cli.isServer)
            buildModel(dataModel.schemas, cli.serverDestinationPath)
        if (cli.isClient)
            buildModel(dataModel.schemas, cli.clientDestinationPath)

        if (cli.isServer) {
            logger.info("Build Paths file")
            buildPaths(dataModel.dslOperations, cli.serverDestinationPath)
        }


        if (cli.isServer) {
            logger.info("Build DSL Files")
            buildDSL(
                dataModel.dslOperations,
                dataModel.schemas.map { it.simplifiedName },
                cli.serverDestinationPath
            )
        }

        logger.info("Build Serialization File")
        buildSerialization()

        if(cli.isServer) {
            logger.info("Build default routing files")
            buildDefaultRouting(dataModel.dslOperations, dataModel.schemas, cli.serverDestinationPath)
        }

        if (cli.isClient) {
            logger.info("Build Client files")
            buildClient(
                dataModel.dslOperations,
                dataModel.schemas.map { it.simplifiedName },
                cli.clientDestinationPath
            )
        }
    }
}