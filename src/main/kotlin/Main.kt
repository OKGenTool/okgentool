import cli.getClient
import cli.logger
import generator.Generator
import parser.Parser


fun main(args: Array<String>) {
    logger().info("Start code generation")

    val client = getClient(args)
    Parser(client.sourcePath)
    Generator(client.destinationPath)
    Generator(client.destinationPath)

    logger().info("Code generation ends")
}


