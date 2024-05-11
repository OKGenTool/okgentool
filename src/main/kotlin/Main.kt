import cli.getClient
import datamodel.Component
import datamodel.ComponentProperties
import datamodel.DataType
import generator.Generator
import generator.builders.model.createModelComponent
import org.slf4j.LoggerFactory
import parser.Parser
import java.util.logging.Logger

private val logger = LoggerFactory.getLogger("Main.kt")

fun main(args: Array<String>) {
//    logger.info("Reading arguments from the command line")
//    val client = getClient(args)
//
//    logger.info("Start parsing the OAD file")
//    val dataModel = Parser(client.sourcePath).getDataModel()
//
//    logger.info("Start code generation")
//    Generator(dataModel, client.destinationPath)
//
//    logger.info("Code generation ends")

    val dataClassWithRequiredIntParameterWithMaxAndMin = Component(
        schemaName = "#/components/schemas/dataClassWithRequiredIntParameterWithMultipleOf",
        parameters = listOf(
            ComponentProperties(
                name = "value",
                dataType = DataType.INTEGER,
                required = true,
                schemaName = "",
                multipleOf = 2
            )
        ),
        simplifiedName = "dataClassWithRequiredIntParameterWithMultipleOf",
        superClassChildSchemaNames = emptyList()
    )

    val dataClassWithRequiredIntParameterWithMaxAndMinComponents = listOf(dataClassWithRequiredIntParameterWithMaxAndMin)

    val fileSpec = createModelComponent(
        dataClassWithRequiredIntParameterWithMaxAndMin,
        dataClassWithRequiredIntParameterWithMaxAndMinComponents
    ).toString()

    print(fileSpec)
}


