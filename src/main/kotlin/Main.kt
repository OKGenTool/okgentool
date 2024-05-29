import cli.getClient
import cli.sourcePath
import generator.Generator
import org.slf4j.LoggerFactory
import parser.Parser

private val logger = LoggerFactory.getLogger("Main.kt")

fun main(args: Array<String>) {
    logger.info("Reading arguments from the command line")
    getClient(args)

    logger.info("Start parsing the OAD file")
    val dataModel = Parser(sourcePath).getDataModel()

    logger.info("Start code generation")
    Generator(dataModel)
        .build()

    logger.info("Code generation ends")
}


