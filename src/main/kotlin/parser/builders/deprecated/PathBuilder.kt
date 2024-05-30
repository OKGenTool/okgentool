package parser.builders.deprecated

//private val logger = LoggerFactory.getLogger("PathBuilder.kt")
//
///**
// * This function receives an OpenAPI object and returns a list of Path objects.
// * Each Path object contains the URL and the list of methods that can be used in that URL.
// */
//fun getPaths(): List<Path> {
//    logger.info("Reading Paths")
//    val pathsList = mutableListOf<Path>()
//    for (path in openAPI.paths) {
//        val methods = mutableListOf<HttpMethods>()
//        val pathItem = path.value
//        if (pathItem.get != null) methods.add(HttpMethods.GET)
//        if (pathItem.post != null) methods.add(HttpMethods.POST)
//        if (pathItem.put != null) methods.add(HttpMethods.PUT)
//        if (pathItem.delete != null) methods.add(HttpMethods.DELETE)
//        if (pathItem.options != null) methods.add(HttpMethods.OPTIONS)
//        if (pathItem.head != null) methods.add(HttpMethods.HEAD)
//        if (pathItem.patch != null) methods.add(HttpMethods.PATCH)
//        if (pathItem.trace != null) methods.add(HttpMethods.TRACE)
//        pathsList.add(Path(path.key, methods))
//    }
//    return pathsList
//}