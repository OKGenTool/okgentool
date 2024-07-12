package generator.builders.routing.routes

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import datamodel.DSLOperation
import generator.capitalize
import generator.model.Packages
import generator.writeFile
import io.ktor.resources.*
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("PathsBuilder.kt")

const val PATHSFILE = "Paths"

fun buildPaths(dslOperations: List<DSLOperation>, destinationPath: String) {
    logger.info("Generating $PATHSFILE file")
    // Define the Paths object
    val pathsObject = TypeSpec.objectBuilder(PATHSFILE)
        .addFunctions(dslOperations)
        .build()

    // Define the Kotlin file
    val kotlinFile = FileSpec.builder(Packages.ROUTES, PATHSFILE)
        .addType(pathsObject)
        .build()

    writeFile(kotlinFile, destinationPath)
}

private fun TypeSpec.Builder.addFunctions(dslOperations: List<DSLOperation>): TypeSpec.Builder {
    dslOperations.forEach {
        this.addType(
            TypeSpec.classBuilder(it.name.capitalize())
                .addAnnotation(
                    AnnotationSpec.builder(Resource::class.asTypeName())
                        .addMember("%S", it.path)
                        .build()
                )
                .addKdoc(
                    """
                         ${it.summary}
                    """.trimIndent()
                )
                .build()
        )

    }
    return this
}

