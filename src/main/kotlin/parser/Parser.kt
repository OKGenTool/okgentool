package parser

import cli.logger
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import parser.builders.getComponents
import parser.builders.getMethods
import parser.builders.getPaths
import datamodel.Component
import datamodel.Method
import datamodel.Path

lateinit var openAPI: OpenAPI
lateinit var components: List<Component>
lateinit var paths: List<Path>
lateinit var methods: List<Method>

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
        val x = openAPI

        logger().info("Reading components")
        components = getComponents()

        logger().info("Reading Paths")
        paths = getPaths()

        logger().info("Reading Methods")
        methods = getMethods()
    }
}