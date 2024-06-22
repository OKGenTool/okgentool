package generator.builders.client

import datamodel.DSLOperation
import generator.builders.client.utils.createResponseStateFile
import generator.writeFile

fun buildClient(operations: List<DSLOperation>, schemaNames: List<String>) {
    val responseStateFileSpec = createResponseStateFile()
    writeFile(responseStateFileSpec)
}