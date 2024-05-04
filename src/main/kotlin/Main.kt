import cli.getClient
import cli.logger
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.Generator
import generator.builders.model.createModelComponent
import parser.Parser


fun main(args: Array<String>) {
//    logger().info("Reading arguments from the command line")
//    val client = getClient(args)
//
//    logger().info("Start parsing the OAD file")
//    val dataModel = Parser(client.sourcePath).getDataModel()
//
//    logger().info("Start code generation")
//    Generator(dataModel, client.destinationPath)
//
//    logger().info("Code generation ends")

    val component = Component(
        schemaName = "#/components/schemas/DataClassWithConstantParameter",
        parameters = listOf(
            ComponentProperties(
                name = "name",
                dataType = DataType.STRING,
                required = true,
                schemaName = ""
            ),
            ComponentProperties(
                name = "value",
                dataType = DataType.STRING,
                required = true,
                schemaName = "",
                arrayItemsSchemaName = "",
                isEnum = true,
                values = listOf("ABC"),
            )
        ),
        simplifiedName = "DataClassWithConstantParameter",
        superClassChildSchemaNames = emptyList()
    )

    val simpleDataClass = Component(
        schemaName = "#/components/schemas/SimpleDataClass",
        parameters = listOf(
            ComponentProperties(
                name = "name",
                dataType = DataType.STRING,
                required = true,
                schemaName = ""
            ),
            ComponentProperties(
                name = "age",
                dataType = DataType.INTEGER,
                required = true,
                schemaName = ""
            )
        ),
        simplifiedName = "SimpleDataClass",
        superClassChildSchemaNames = emptyList()
    )

    val fileSpec = createModelComponent(component, listOf(component)).toString()
    print(fileSpec)
}


