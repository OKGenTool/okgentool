package generator.builders.DSL

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import datamodel.DSLOperation
import generator.model.Packages
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("DSLBuilder.kt")

fun buildDSLOperations(dslOperations: List<DSLOperation>, basePath: String) {
    for (operation in dslOperations) {
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name)
            .addType(
                TypeSpec.classBuilder(operation.name)
                    .addModifiers(KModifier.DATA)
                    .build()
            )
            .build()
        writeFile(fileSpec, basePath)
    }
}