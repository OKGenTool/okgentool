package org.example.petstoreserver.mutable.routing.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.petstoreserver.mutable.services.PetServices
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("PetRoute.kt")
val petServices = PetServices()

fun OKGenDSL.petDSLRouting() {
    route.addPet {
        try {
            val pet = request.body
            val newPet = petServices.addPet(pet)
            response.addPetResponse200(newPet)
        } catch (ex: Exception) {
            response.addPetResponse405()
        }
    }

    route.findPetsByStatus {
        val status = request.result
        when (status) {
            is ReadRequestResult.Success -> {
                val pets = petServices.getPetsByStatus(status.value)
                if (pets.isNotEmpty())
                    response.findPetsByStatusResponse200(pets)
                else {
                    logger.error("No pets found")
                    response.findPetsByStatusResponse400()
                }
            }

            is ReadRequestResult.Failure -> {
                when (status.error) {
                    is RequestErrorInvalidArgument -> {
                        logger.error("Invalid Request Argument: ${status.error.msg}")
                    }

                    else -> {
                        logger.error("Invalid Request: ${status.error.msg}")
                    }
                }
                response.findPetsByStatusResponse400()
            }
        }
    }

    route.findPetsByTags {
        val query = request.query
        logger.info(query.toString())
        val allPets = petServices.getAllPets()
        if (allPets.isEmpty())
            response.findPetsByTagsResponse400()
        else
            response.findPetsByTagsResponse200(allPets)
    }

    route.getPetById {
        val param = request.pathParam
        if (param == null) {
            response.getPetByIdResponse400()
            return@getPetById
        }
        val pet = petServices.getPet(param)
        if (pet != null)
            response.getPetByIdResponse200(pet)
        else
            response.getPetByIdResponse404()
    }

    route.updatePetWithForm{
        request.paramPetId
    }
}


