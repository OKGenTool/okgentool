package org.example.petstoreserver.mutable.routing.routes

import org.example.petstoreserver.gen.dsl.OkGenDsl
import org.example.petstoreserver.mutable.services.PetServices
import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger("PetRoute.kt")
val petServices = PetServices()


// Use this function to write the demo
fun OkGenDsl.petDSLRouting() {

}


/**
 * After the demo the final code should look like this
 */
fun OkGenDsl.demoFinalCode() {
    route.addPet {
        val pet = request.pet
        if (pet == null || pet.id != null) {
            response.addPetResponse405() // Invalid input
            return@addPet
        }
        val newPet = petServices.addPet(pet)
        response.addPetResponse200(newPet) // Successful operation
    }

    route.getPetById {
        val petId = request.petId ?: run {
            response.getPetByIdResponse400() // Invalid ID supplied
            return@getPetById
        }

        val pet = petServices.getPet(petId)
        if (pet != null)
            response.getPetByIdResponse200(pet) // successful operation
        else
            response.getPetByIdResponse404() // Pet not found
    }

    route.updatePet {
        val pet = request.pet ?: run {
            response.updatePetResponse405() //Validation exception
            return@updatePet
        }

        if (pet.id == null) {
            response.updatePetResponse400() // Invalid ID supplied
            return@updatePet
        }

        when (val foundPet = petServices.updatePet(pet)) {
            null -> response.updatePetResponse404() // Pet not found
            else -> response.updatePetResponse200(foundPet) // Successful operation
        }
    }
}


