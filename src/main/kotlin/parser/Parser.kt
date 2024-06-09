package parser

import datamodel.DataModel
import datamodel.InlineSchema
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import parser.builders.buildSchemas
import parser.builders.buildOperations

lateinit var openAPI: OpenAPI

class Parser(sourceFilePath: String) {

    init {
        val parseOptions = ParseOptions()
        parseOptions.isResolve = true

        /**
         * parseOptions.isResolveFully = true
         * This parameter duplicates schemas and requestBodies. Use with caution.
         * See more: https://github.com/swagger-api/swagger-parser?tab=readme-ov-file#2-resolvefully
         */

        parseOptions.isResolveCombinators = true
        parseOptions.isResolveRequestBody = true
        parseOptions.isValidateExternalRefs = true
        parseOptions.isFlatten = true
        parseOptions.isFlattenComposedSchemas = true
        parseOptions.isCamelCaseFlattenNaming = true
        openAPI = OpenAPIParser().readLocation(sourceFilePath, null, parseOptions).openAPI
    }

    fun getDataModel(): DataModel {
        val operations = buildOperations()
        val inlineSchemas: MutableList<InlineSchema> = mutableListOf()

        operations.map {
            it.inlineSchemas.map {
                inlineSchemas.add(it)
            }
        }
        val components = getComponents(inlineSchemas)

        return DataModel(components, operations)
    }
}