package parser

import datamodel.DataModel
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import parser.builders.getComponents
import parser.builders.getMethods
import parser.builders.getOperations
import parser.builders.getPaths

lateinit var openAPI: OpenAPI

class Parser(sourceFilePath: String) {

    init {
        val parseOptions = ParseOptions()
        parseOptions.isResolve = true
        parseOptions.isResolveFully = true
        parseOptions.isResolveCombinators = true
        parseOptions.isResolveRequestBody = true
        parseOptions.isValidateExternalRefs = true
        parseOptions.isFlatten = true
        parseOptions.isFlattenComposedSchemas = true
        parseOptions.isCamelCaseFlattenNaming = true
        openAPI = OpenAPIParser().readLocation(sourceFilePath, null, parseOptions).openAPI
    }

    fun getDataModel(): DataModel {
        val paths = getPaths()
        return DataModel(getComponents(), getMethods(paths), paths, getOperations())
    }
}