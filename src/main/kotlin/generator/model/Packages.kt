package generator.model

import cli.packageName

object Packages {
    val BASE = "${packageName}gen"
    val DSL = "${BASE}.dsl"
    val DSLOPERATIONS = "$DSL.operations"
    val ROUTING = "$BASE.routing"
    val MODEL = "$ROUTING.model"
    val PLUGINS = "$ROUTING.plugins"
    val ROUTES = "$ROUTING.routes"
    val UTILS = "$BASE.utils"
}