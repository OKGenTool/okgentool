package generator.builders.dsl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import datamodel.*
import generator.capitalize
import generator.model.Packages
import generator.model.Parameter
import generator.model.Visibility
import io.ktor.http.ContentType.Application.OctetStream
import io.ktor.server.application.*
import org.slf4j.LoggerFactory
import output.writeFile

private val logger = LoggerFactory.getLogger("DSLBuilder.kt")

//TODO receive dslOperations instead of the entire dataModel
fun buildDSLOperations(dataModel: DataModel, basePath: String) {
    val dslOperations = dataModel.dslOperations
    for (operation in dslOperations) {
        if (operation.name.equals("createUsersWithListInput")) logger.info("createUsersWithListInput")
        val fileSpec = FileSpec.builder(Packages.DSLOPERATIONS, operation.name.capitalize())

        var requestParameter: Parameter? = null

        if (operation.requestBody != null) {
            requestParameter = getParameter(operation.requestBody)
            fileSpec.addType(
                getRequest(listOf(requestParameter), operation.name)
            )
        }

        if (operation.responses != null)
            fileSpec.addType(getResponse(operation))

        //Get Operation main class
        fileSpec.addType(
            getMainClass(
                listOf(
                    requestParameter,
                    Parameter(
                        "call",
                        ApplicationCall::class.asTypeName(),
                        Visibility.PRIVATE
                    )
                ),
                operation.name
            )
        )

        writeFile(fileSpec.build(), basePath)
    }
}

private fun getMainClass(parameters: List<Parameter?>, operationName: String): TypeSpec {
    val mainClass = TypeSpec.classBuilder(operationName.capitalize())

    if (!parameters.isEmpty()) {
        mainClass.getConstructor(parameters)
    }

    return mainClass.build()
}

private fun getResponse(operation: DSLOperation): TypeSpec {
    val propertySpecs = getProperties(operation.name, operation.responses!!)

    val responseBuilder = TypeSpec.classBuilder("${operation.name.capitalize()}Response").addModifiers(KModifier.DATA)

    //Build constructor
    val const = FunSpec.constructorBuilder()
    propertySpecs?.map {
        responseBuilder.addProperty(it)
        const.addParameter(it.name, it.type)
    }
    responseBuilder.primaryConstructor(const.build())

    return responseBuilder.build()
}

private fun getProperties(operationName: String, responses: List<ResponseNew>): List<PropertySpec> {
    return responses.map { response ->
        val varName = "${operationName}Response${response.statusCode.capitalize()}"

        val varType: TypeName
        val schemaRef = response.schemaRef
        if (schemaRef.isNullOrEmpty()) {
            varType = LambdaTypeName.get(returnType = UNIT).copy(suspending = true)
        } else {
            val simpleName = SchemaProps.getRefSimpleName(schemaRef)
            varType = LambdaTypeName.get(
                parameters = arrayOf(ClassName(Packages.MODEL, simpleName.capitalize())),
                returnType = UNIT
            ).copy(suspending = true)

        }
        PropertySpec
            .builder(varName, varType)
            .initializer(varName)
            .build()

    }
}

private fun getRequest(parameters: List<Parameter>, operationName: String): TypeSpec {
    return TypeSpec.classBuilder("${operationName}Request".capitalize())
        .addModifiers(KModifier.DATA)
        .getConstructor(parameters)
        .build()
}

private fun TypeSpec.Builder.getConstructor(parameters: List<Parameter?>): TypeSpec.Builder {
    val constructor = FunSpec.constructorBuilder()
    val properties: MutableList<PropertySpec> = mutableListOf()

    parameters.map {
        if (it != null) {
            constructor.addParameter(it.name, it.type)
            properties.add(
                PropertySpec.builder(it.name, it.type, it.visibility.modifier)
                    .initializer(it.name)
                    .build()
            )
        }
    }

    return this.primaryConstructor(
        constructor.build()
    ).addProperties(properties)
}


//fun getClassName(dataType: DataType, subClass: ClassName?): ClassName {
//    if (dataType == DataType.ARRAY) return dataType.kotlinType.parameterizedBy(subClass!!).rawType
//    return dataType.kotlinType
//}

private fun getParameter(body: BodyNew): Parameter {
    var name: String = ""
    var type: TypeName? = null

    when (body) {
        is BodyRef -> {
            name = body.schemaRef
            type = ClassName(
                Packages.MODEL, body.schemaRef.capitalize()
            )
        }

        is BodyObj -> {
            name = if (body.contentTypes[0] == OctetStream.toString()) "binFile"
            else "prop"

            type = body.dataType.kotlinType
        }

        //When is a collection of regular POJOs
        is BodyCollPojo -> {
            //Use the first tag (if any) to build the var name
            name = body.tags?.firstOrNull()?.let { "${it}List" } ?: "list"
            type = List::class.asTypeName().parameterizedBy(body.dataType.kotlinType)
        }

        is BodyCollRef -> {
            name = "${body.className}List"
            type = List::class.asTypeName().parameterizedBy(
                ClassName(
                    Packages.MODEL, body.className.capitalize()
                )
            )
        }
    }

    return Parameter(name, type!!)
}


