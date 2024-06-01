package org.example.petstoreserver.mutable.routing.routes

import org.example.petstoreserver.mutable.services.PetServices
import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger("PetRoute.kt")
val petServices = PetServices()


//TODO use this function to make the demo
fun OkGenDsl.petDSLRouting() {


}


/**
 * After the demo the final code should look like this
 */
fun OkGenDsl.demoFinalCode() {
    route.addPet {
        try {
            val pet = request.pet
            val newPet = petServices.addPet(pet)
            response.addPetResponse200(newPet)
        } catch (ex: Exception) {
            response.addPetResponse405()
        }
    }

    route.getPetById {
        try {
            val petId = request.petId
            val pet = petServices.getPet(petId)
            if (pet != null)
                response.getPetByIdResponse200(pet)
            else
                response.getPetByIdResponse404()
        } catch (ex: Exception) {
            response.getPetByIdResponse400()
        }
    }
}


