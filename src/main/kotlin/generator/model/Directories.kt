package generator.model

data class Directories(
    val root: String,
) {
    val gen = "$root/gen"
    val dsl = "$root/gen/dsl"
    val routing = "$root/gen/routing"
    val model = "$root/gen/routing/model"
    val plugins = "$root/gen/routing/plugins"
    val routes = "$root/gen/routing/routes"
    val utils = "$root/gen/utils"
}
