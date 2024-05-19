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

        if (operation.requestBody != null)
            fileSpec.addType(getRequest(operation.name, operation.requestBody!!))

        if (operation.responses != null) {
            //TODO add response Class
            fileSpec.addType(getResponse(operation))
        }


        writeFile(fileSpec.build(), basePath)
    }
}

private fun getResponse(operation: DSLOperation): TypeSpec {
    val propertySpecs = getProperties(operation.name, operation.responses!!)

    val responseBuilder = TypeSpec.classBuilder("${operation.name.capitalize()}Response").addModifiers(KModifier.DATA)

    //Build constructor
    val const = FunSpec.constructorBuilder()
    propertySpecs?.map { property ->
        responseBuilder.addProperty(property)
        const.addParameter(property.name, property.type)
    }
    responseBuilder.primaryConstructor(const.build())

    return responseBuilder.build()
}

private fun getProperties(operationName: String, responses: List<ResponseNew>): List<PropertySpec>? {
    return responses.map { response ->
        val varName = "${operationName}Response${response.statusCode}"

//        val varType = String::class //TODO get the proper data model
        var varType: TypeName
        val schemaRef = response.schemaRef
        if (schemaRef.isNullOrEmpty()) {
            varType = String::class.asTypeName()
        } else {
            val simpleName = SchemaProps.getRefSimpleName(schemaRef)
            varType = ClassName(
                Packages.MODEL, simpleName.capitalize()
            )
        }
        PropertySpec.builder(varName, varType).initializer(varName).build()

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
            varName = if (body.contentTypes[0] == OctetStream.toString()) "binFile"
            else "prop"

            varType = body.dataType.kotlinType
        }

        //When is a collection of regular POJOs
        is BodyCollPojo -> {
            //Use the first tag (if any) to build the var name
            varName = body.tags?.firstOrNull()?.let { "${it}List" } ?: "list"
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
