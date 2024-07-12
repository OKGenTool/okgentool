package generator.model

import cli.cliPackageName

object Packages {
    val BASE = "${cliPackageName}gen"
    val DSL = "${BASE}.dsl"
    val DSLOPERATIONS = "$DSL.operations"
    val ROUTING = "$BASE.routing"
    val MODEL = "$ROUTING.model"
    val PLUGINS = "$ROUTING.plugins"
    val ROUTES = "$ROUTING.routes"
    val UTILS = "$BASE.utils"
    val DEFAULT_ROUTING = "$BASE.defaultRouting"
    val DEFAULT_ROUTING_EXAMPLES = "$DEFAULT_ROUTING.examples"
    val CLIENT = "$BASE.client"
}