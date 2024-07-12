package generator.builders.dsl

import datamodel.DSLOperation
import generator.model.Parameter
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("DslBuilder.kt")

fun buildDSL(dslOperations: List<DSLOperation>, componentNames: List<String>, destinationPath: String) {
    val paramsToImportInOkGenDSL: MutableList<Parameter> = mutableListOf()

    buildDSLOperations(dslOperations, paramsToImportInOkGenDSL, destinationPath)
    buildDslControls(destinationPath)
    buildUnsafe(destinationPath)
    buildOkGenDsl(dslOperations, componentNames, paramsToImportInOkGenDSL, destinationPath)
}
