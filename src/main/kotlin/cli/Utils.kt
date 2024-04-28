package cli

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun logger(): Logger {
    val fileName = Thread.currentThread().stackTrace[2].fileName
    return LoggerFactory.getLogger(fileName)
}
