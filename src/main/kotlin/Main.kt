import client.getClient
import parser.Parser
import parser.builders.getComponents
import parser.builders.getMethod
import parser.builders.getPaths
import parser.openAPI

fun main(args: Array<String>) {
    val client = getClient(args)
    Parser(client.sourcePath)

//    val paths = getPaths()
//
//    for (path in paths) {
//        for (method in path.methods) {
//            val aux = getMethod(path, method)
//            println()
//        }
//    }
//
//    val aux = getComponents()
//    val x = openAPI
}


