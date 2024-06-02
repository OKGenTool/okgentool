package org.example.petstoreserver.mutable.repository

import org.example.petstoreserver.gen.routing.model.Category
import org.example.petstoreserver.gen.routing.model.Pet
import org.example.petstoreserver.gen.routing.model.PetStatus
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(Repository::class.java.simpleName)!!

class Repository {
    private val categories = initiateCategories()
    private val pets = initiatePets(categories)

    fun addPet(pet: Pet): Pet {
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

    fun updatePet(updatedPet: Pet): Pet? {
        val actualPet = getPet(updatedPet.id!!) ?: return null

        val newPet = Pet(
            actualPet.id,
            updatedPet.name,
            updatedPet.category,
            updatedPet.photoUrls,
            updatedPet.tags,
            updatedPet.status,
        )

        pets.remove(actualPet)
        pets.add(newPet)
        return newPet
    }

    fun getPet(id: Long): Pet? {
        return pets.find { it.id == id }
    }

    fun getAllPets(): List<Pet> = pets

}

private fun initiateCategories(): List<Category> = listOf(
    Category(1, "Dogs"),
    Category(2, "Cats"),
    Category(3, "Rabbits"),
    Category(4, "GoldFish"),
    Category(5, "Hamsters"),
)

private fun initiatePets(categories: List<Category>): MutableList<Pet> = mutableListOf(
    Pet(
        1,
        "Max",
        categories.find { it.id?.toInt() == 1 },
        listOf("url photos"),
        null,
        PetStatus.available
    ),
    Pet(
        2,
        "Bella",
        categories.find { it.id?.toInt() == 2 },
        listOf("url photos"),
        null,
        PetStatus.available
    ),
    Pet(
        3,
        "Charlie",
        categories.find { it.id?.toInt() == 3 },
        listOf("url photos"),
        null,
        PetStatus.sold
    ),
    Pet(
        4,
        "Luna",
        categories.find { it.id?.toInt() == 5 },
        listOf("url photos"),
        null,
        PetStatus.pending
    ),
)