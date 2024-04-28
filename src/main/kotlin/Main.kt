import cli.getClient
import cli.logger
import generator.Generator
import parser.Parser


fun main(args: Array<String>) {
    logger().info("Reading arguments from the command line")
    val client = getClient(args)

    logger().info("Start parsing the OAD file")
    Parser(client.sourcePath)

    logger().info("Start code generation")
    Generator(client.destinationPath)

    logger().info("Code generation ends")
}


