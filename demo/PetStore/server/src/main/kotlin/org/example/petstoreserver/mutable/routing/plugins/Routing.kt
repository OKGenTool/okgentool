package org.example.petstoreserver.mutable.routing.plugins

import gen.dsl.OkGenDsl
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.petstoreserver.mutable.routing.routes.petDSLRouting


//fun Application.configureManualRouting() {
//    routing {
//        petManualRouting()
//    }
//}

fun Application.configureMutableRouting() {
    routing {
        OkGenDsl(this).petDSLRouting()
    }
}
