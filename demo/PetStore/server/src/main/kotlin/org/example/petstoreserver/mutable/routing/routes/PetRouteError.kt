package org.example.petstoreserver.mutable.routing.routes

import gen.routing.model.Pet
import org.example.petstoreserver.mutable.Either

sealed class PetRouteError {
    object InvalidInput : PetRouteError()
    object InvalidId : PetRouteError()
    object PetNotFound : PetRouteError()
    object ValidationException : PetRouteError()
}

typealias PetRouteResult = Either<PetRouteError, Pet>
