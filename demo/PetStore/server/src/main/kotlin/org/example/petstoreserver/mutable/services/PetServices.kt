package org.example.petstoreserver.mutable.services

import org.example.petstoreserver.gen.routing.model.Pet
import org.example.petstoreserver.mutable.repository.Repository
import org.example.petstoreserver.mutable.routing.routes.PetRouteResult
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(PetServices::class.java.simpleName)

class PetServices {

    val repository = Repository()

    fun addPet(pet: Pet?): Pet {
        return repository.addPet(pet)
    }

    fun updatePet(pet: Pet): PetRouteResult {
        return repository.updatePet(pet)
    }

    fun getPetsByStatus(status: String): List<Pet> =
        repository.getAllPets()

    fun getAllPets(): List<Pet> = repository.getAllPets()

    fun getPet(petId: Long?): Pet? = repository.getPet(petId)

}