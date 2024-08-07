import cli.getCliModel
import generator.Generator
import org.slf4j.LoggerFactory
import parser.Parser

private val logger = LoggerFactory.getLogger("Main.kt")

fun main(args: Array<String>) {
    logger.info("Reading arguments from the command line")
    val cli = getCliModel(args)

    logger.info("Start parsing the OAD file")
    val dataModel = Parser(cli.sourcePath).buildDataModel()

    logger.info("Start code generation")
    Generator(dataModel, cli)
        .build()

    logger.info("Code generation ends")
}


