package generator.builders.routing.routes

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import generator.model.Packages
import output.writeFile

const val PACKAGENAME = Packages.ROUTES
const val READREQUESTRESULT = "ReadRequestResult"
const val REQUESTERROR = "RequestError"

fun buildReadRequestResult(basePath: String) {

    // Define the ReadRequestResult sealed class
    val readRequestResultClass = TypeSpec.classBuilder(READREQUESTRESULT)
        .addModifiers(KModifier.SEALED)
        .addTypeVariable(TypeVariableName("out R"))
        .addType(
            TypeSpec.classBuilder("Failure")
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("error", ClassName(PACKAGENAME, REQUESTERROR))
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("error", ClassName(PACKAGENAME, REQUESTERROR))
                        .initializer("error")
                        .build()
                )
                .superclass(ClassName(PACKAGENAME, READREQUESTRESULT).parameterizedBy(NOTHING))
                .build()
        )
        .addType(
            TypeSpec.classBuilder("Success")
                .addModifiers(KModifier.DATA)
                .addTypeVariable(TypeVariableName("out R"))
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("res", TypeVariableName("R"))
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("res", TypeVariableName("R"))
                        .initializer("res")
                        .build()
                )
                .superclass(ClassName(PACKAGENAME, READREQUESTRESULT).parameterizedBy(TypeVariableName("R")))
                .build()
        )
        .build()

    // Define the RequestError open class
    val requestErrorClass = TypeSpec.classBuilder(REQUESTERROR)
        .addModifiers(KModifier.OPEN)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("msg", String::class)
                .build()
        )
        .addProperty(
            PropertySpec.builder("msg", String::class)
                .addModifiers(KModifier.OPEN)
                .initializer("msg")
                .build()
        )
        .build()

    // Define the RequestErrorInvalidArgument class
    val requestErrorInvalidArgumentClass = TypeSpec.classBuilder("RequestErrorInvalidArgument")
        .superclass(ClassName(PACKAGENAME, REQUESTERROR))
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("msg", String::class)
                .build()
        )
        .addSuperclassConstructorParameter("msg")
        .addProperty(
            PropertySpec.builder("msg", String::class)
                .initializer("msg")
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
        .build()

    // Create the Kotlin file
    val file = FileSpec.builder(PACKAGENAME, READREQUESTRESULT)
        .addType(readRequestResultClass)
        .addType(requestErrorClass)
        .addType(requestErrorInvalidArgumentClass)
        .build()

    writeFile(file, basePath)

}