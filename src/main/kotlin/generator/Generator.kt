package generator

import generator.builders.buildDirectory
import generator.builders.buildModel

class Generator(destinationPath: String) {
    init {
        val directories = buildDirectory(destinationPath)
        buildModel(directories.base)
    }
}