package org.example.petstoreserver.mutable.routing.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureManualRouting() {
    routing {
        petManualRouting()
    }
}

fun Application.configureMutableRouting() {
    routing {
        OKGenDSL(this).petDSLRouting()
    }
}
