package generator.builders.dsl

import datamodel.DSLOperation
import generator.model.Parameter
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("DslBuilder.kt")

fun buildDSL(dslOperations: List<DSLOperation>, componentNames: List<String>) {
    val paramsToImportInOkGenDSL: MutableList<Parameter> = mutableListOf()

    buildDSLOperations(dslOperations,paramsToImportInOkGenDSL)
    buildDslControls()
    buildUnsafe()
    buildOkGenDsl(dslOperations, componentNames, paramsToImportInOkGenDSL)
}
