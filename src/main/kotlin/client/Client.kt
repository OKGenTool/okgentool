package client

data class Client(
    val sourcePath: String,
    val destinationPath: String,
)

fun getClient(args: Array<String>): Client {
    logger().info("Entered in client")

    lateinit var sourcePath: String
    lateinit var destinationPath: String

    for (i in args.indices){
        when(args[i]){
            "-s" -> sourcePath = args[i + 1]
            "-g" -> destinationPath = args[i + 1] //TODO why -g? Maybe -d could be a better arg for "destination"
            else -> { }
        }
    }

    return Client(sourcePath, destinationPath)
}
