package org.example.petstoreserver.mutable.routing.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.petstoreserver.gen.dsl.OKGenDSL
import org.example.petstoreserver.mutable.routing.routes.anotherRouting
import org.example.petstoreserver.mutable.routing.routes.petManualRouting
import org.example.petstoreserver.mutable.routing.routes.petDSLRouting


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
