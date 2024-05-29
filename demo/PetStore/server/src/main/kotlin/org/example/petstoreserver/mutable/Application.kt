package org.example.petstoreserver.mutable

import SERVER_PORT
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.resources.*
import org.example.petstoreserver.mutable.routing.plugins.configureMutableRouting
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Application.kt")

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Resources)
//    configureGenSerialization()
    configureMutableRouting()       //Mutable routing comes first
//    configureGenDefaultRouting()    //Generated routing comes last
}
