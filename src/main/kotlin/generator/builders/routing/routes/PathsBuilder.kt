package generator.builders.routing.routes

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import generator.model.Packages
import io.ktor.resources.*

import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("PathsBuilder.kt")

fun buildPaths(basePath: String) {


    // Define the addPet class
    val addPetClass = TypeSpec.classBuilder("AddPet")
//        .addModifiers(KModifier.CLASS)
        .addAnnotation(
            AnnotationSpec.builder(Resource::class.asTypeName())
                .addMember("%S", "/pet")
                .build()
        )
        .addKdoc(
            """
             Add a new pet to the store
             @param pet Create a new pet in the store
        """.trimIndent()
        )
        .build()

    // Define the Paths object
    val pathsObject = TypeSpec.objectBuilder("Paths")
        .addType(addPetClass)
        .build()

    // Define the Kotlin file
    val kotlinFile = FileSpec.builder(Packages.ROUTES, "Paths")
        .addType(pathsObject)
        .build()

    writeFile(kotlinFile, basePath)

}

