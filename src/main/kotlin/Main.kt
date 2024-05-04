import cli.getClient
import cli.logger
import generator.Generator
import parser.Parser


fun main(args: Array<String>) {
    logger().info("Reading arguments from the command line")
    val client = getClient(args)

    logger().info("Start parsing the OAD file")
    val dataModel = Parser(client.sourcePath).getDataModel()

    logger().info("Start code generation")
    Generator(dataModel, client.destinationPath)

    logger().info("Code generation ends")
}


