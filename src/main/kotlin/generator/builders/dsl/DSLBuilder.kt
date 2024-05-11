package generator.builders.dsl

import com.squareup.kotlinpoet.*
import datamodel.Body
import datamodel.DSLOperation
import generator.capitalize
import generator.model.Packages
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("DSLBuilder.kt")
//private const val TMP_PACKAGE = "generator.builders.generated"

fun buildDSLOperations(dslOperations: List<DSLOperation>, basePath: String) {
//    example(basePath)
    for (operation in dslOperations) {
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())
//            .addType(
//                TypeSpec.classBuilder(operation.name)
//                    .addModifiers(KModifier.DATA)
//                    .build()
//            )

        if (operation.requestBody != null)
            fileSpec.addType(getRequest(operation.name, operation.requestBody!!))

        writeFile(fileSpec.build(), basePath)
    }
}

private fun getRequest(operationName: String, body: Body): TypeSpec {
//    val parameter = ParameterSpec
//        .builder("myProp", String::class)
//        .build()
//
//    val property = PropertySpec
//        .builder("myProp", String::class)
//        .initializer("\"\"")
//        .build()


    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("myProp", String::class)
                .build()
        )
        .addProperty(
            PropertySpec.builder("myProp", String::class)
                .initializer("myProp")
                .build()
        )
        .build()
}


fun example(basePath: String) {
    val greeterClass = ClassName(Packages.DSLOPERATIONS, "Greeter")
    val file = FileSpec.builder(Packages.DSLOPERATIONS, "HelloWorld")
        .addType(
            TypeSpec.classBuilder("Greeter")
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("name", String::class)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("name", String::class)
                        .initializer("name")
                        .build()
                )
                .addFunction(
                    FunSpec.builder("greet")
                        .addStatement("println(%P)", "Hello, \$name")
                        .build()
                )
                .build()
        )
        .addFunction(
            FunSpec.builder("main")
                .addParameter("args", String::class, KModifier.VARARG)
                .addStatement("%T(args[0]).greet()", greeterClass)
                .build()
        )
        .build()

    writeFile(file, basePath)
}
