import client.getClient
import generator.Generator
import parser.Parser

fun main(args: Array<String>) {
    val client = getClient(args)
    Parser(client.sourcePath)
    Generator(client.destinationPath)
}


