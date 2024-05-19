package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.capitalize
import generator.model.Packages
import io.ktor.http.ContentType.Application.OctetStream
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("DSLBuilder.kt")

//TODO receive dslOperations instead of the entire dataModel
fun buildDSLOperations(dataModel: DataModel, basePath: String) {
    val dslOperations = dataModel.dslOperations
    for (operation in dslOperations) {
        if (operation.name.equals("createUsersWithListInput")) logger.info("createUsersWithListInput")
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())
//            .addType(
//                TypeSpec.classBuilder(operation.name)
//                    .addModifiers(KModifier.DATA)
//                    .build()
//            )

        if (operation.requestBody != null) fileSpec.addType(getRequest(operation.name, operation.requestBody!!))

        writeFile(fileSpec.build(), basePath)
    }
}

private fun getRequest(operationName: String, body: BodyNew): TypeSpec {
    var varName: String = ""
    var varType: TypeName? = null
    var typeName: TypeName? = null

    when (body) {
        is BodyRef -> {
            varName = body.schemaRef
            varType = ClassName(
                Packages.MODEL, body.schemaRef.capitalize()
            )
        }

        is BodyObj -> {
            varName =
                if (body.returnType[0] == OctetStream.toString()) "binFile"
                else "myProp" //TODO what scenarios exists?

            varType = body.dataType.kotlinType
        }

        //When is a collection of regular POJOs
        is BodyCollPojo -> {
            varName = "someCollection" //TODO
            varType = List::class.asTypeName().parameterizedBy(body.dataType.kotlinType)
        }

        is BodyCollRef -> {
            varName = "${body.className}List"
            varType = List::class.asTypeName().parameterizedBy(
                ClassName(
                    Packages.MODEL, body.className.capitalize()
                )
            )
        }
    }
    return TypeSpec.classBuilder("${operationName}Request".capitalize()).addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder().addParameter(varName, varType!!).build()
        ).addProperty(
            PropertySpec.builder(varName, varType).initializer(varName).build()
        ).build()
}

fun getClassName(dataType: DataType, subClass: ClassName?): ClassName {
    if (dataType == DataType.ARRAY) return dataType.kotlinType.parameterizedBy(subClass!!).rawType
    return dataType.kotlinType
}


/**
 * Example from https://square.github.io/kotlinpoet/
 */
fun example(basePath: String) {
    val greeterClass = ClassName(Packages.DSLOPERATIONS, "Greeter")
    val file = FileSpec.builder(Packages.DSLOPERATIONS, "HelloWorld").addType(
        TypeSpec.classBuilder("Greeter").addModifiers(KModifier.DATA).primaryConstructor(
            FunSpec.constructorBuilder().addParameter("name", String::class).build()
        ).addProperty(
            PropertySpec.builder("name", String::class).initializer("name").build()
        ).addFunction(
            FunSpec.builder("greet").addStatement("println(%P)", "Hello, \$name").build()
        ).build()
    ).addFunction(
        FunSpec.builder("main").addParameter("args", String::class, KModifier.VARARG)
            .addStatement("%T(args[0]).greet()", greeterClass).build()
    ).build()

    writeFile(file, basePath)
}
