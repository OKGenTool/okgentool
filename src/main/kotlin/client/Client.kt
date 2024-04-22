package client

data class Client(
    val sourcePath: String,
    val destinationPath: String,
)

fun getClient(args: Array<String>): Client {
    lateinit var sourcePath: String
    lateinit var destinationPath: String

    for (i in args.indices){
        when(args[i]){
            "-s" -> sourcePath = args[i + 1]
            "-g" -> destinationPath = args[i + 1]
            else -> { }
        }
    }

    return Client(sourcePath, destinationPath)
}
