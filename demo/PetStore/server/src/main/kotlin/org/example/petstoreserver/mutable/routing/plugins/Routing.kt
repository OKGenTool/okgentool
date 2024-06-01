package org.example.petstoreserver.mutable.routing.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.example.petstoreserver.gen.dsl.OkGenDsl
import org.example.petstoreserver.mutable.routing.routes.petDSLRouting

fun Application.configureMutableRouting() {
    routing {
        OkGenDsl(this).petDSLRouting()
    }
}
