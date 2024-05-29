package org.example.petstoreserver.mutable.routing.routes

import org.example.petstoreserver.gen.routing.model.Pet
import org.example.petstoreserver.mutable.Either

sealed class PetRouteError {
    object InvalidInput : PetRouteError()
    object InvalidId : PetRouteError()
    object PetNotFound : PetRouteError()
    object ValidationException : PetRouteError()
}

typealias PetRouteResult = Either<PetRouteError, Pet>
