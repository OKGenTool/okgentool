package generator.builders.client

import datamodel.DSLOperation
import generator.builders.client.utils.createOkGenClientFile
import generator.builders.client.utils.createResponseStateFile
import generator.writeFile

fun buildClient(operations: List<DSLOperation>, schemaNames: List<String>, destinationPath: String) {
    val responseStateFileSpec = createResponseStateFile()
    writeFile(responseStateFileSpec, destinationPath)

    val okGenClientFileSpec = createOkGenClientFile(operations, schemaNames)
    writeFile(okGenClientFileSpec, destinationPath)
}