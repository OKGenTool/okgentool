package org.example.petstoreserver.mutable.routing.routes

import org.example.petstoreserver.gen.dsl.OkGenDsl
import org.example.petstoreserver.mutable.services.PetServices
import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger("PetRoute.kt")
val petServices = PetServices()


//TODO This is the Demo. Delete at the end
fun OkGenDsl.petDSLRouting() {
    route.addPet {
        try {
            val pet = request.pet
            val newPet = petServices.addPet(pet)
            response.addPetResponse200(newPet)
        }catch (ex:Exception){
            response.addPetResponse405()
        }
    }


//    route.postPet {
//        try {
//            val pet = request.pet
//            val newPet = petServices.addPet(pet)
//            response.postPetResponse200(newPet)
//        } catch (ex: Exception) {
//            response.postPetResponse405()
//        }
//    }
}


