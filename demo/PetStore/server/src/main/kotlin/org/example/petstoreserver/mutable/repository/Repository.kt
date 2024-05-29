package org.example.petstoreserver.mutable.repository

import org.example.petstoreserver.gen.routing.model.Pet
import org.example.petstoreserver.mutable.failure
import org.example.petstoreserver.mutable.routing.routes.PetRouteError
import org.example.petstoreserver.mutable.routing.routes.PetRouteResult
import org.example.petstoreserver.mutable.success
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(Repository::class.java.simpleName)!!

class Repository {
    private val pets = mutableListOf<Pet>()

    //TODO consider concurrency
    fun addPet(pet: Pet): Pet {
        //TODO throw exception if already as petId
        if (pet.id != null) {
            throw RuntimeException("Trying to add a Pet with ID")
        }
        val petId: Long = (pets.count() + 1).toLong()

        val newPet = Pet(
            petId,
            pet.name,
            pet.category,
            pet.photoUrls,
            pet.tags,
            pet.status
        )
        pets.add(newPet)
        logger.info("New Pet Added: $newPet")
        return newPet
    }

    fun updatePet(pet: Pet): PetRouteResult {
        val foundPet = getPet(pet.id!!)
        return if (foundPet == null) failure(PetRouteError.PetNotFound)
        else success(foundPet)
    }

    fun getPet(id: Long): Pet? {
        return pets.find { it.id == id }
    }

    fun getAllPets(): List<Pet> = pets


}