package org.example.petstoreserver.mutable.repository



import org.example.petstoreserver.mutable.routing.models.Pet
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
        val petId = pets.count() + 1

        val newPet = Pet(
            pet.name,
            pet.photoUrls,
            petId.toLong(),
            pet.category,
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