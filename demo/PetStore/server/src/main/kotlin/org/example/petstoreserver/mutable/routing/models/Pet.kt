package org.example.petstoreserver.mutable.routing.models


import gen.routing.model.Tag
import kotlinx.serialization.Serializable
import java.util.*


/**
 * 
 * @param name 
 * @param photoUrls 
 * @param id 
 * @param category 
 * @param tags 
 * @param status pet status in the store
 */
@Serializable
data class Pet(
    val name: kotlin.String,
    val photoUrls: kotlin.collections.List<kotlin.String>,
    val id: kotlin.Long? = null,
    val category: Locale.Category? = null,
    val tags: kotlin.collections.List<Tag>? = null,
    /* pet status in the store */
    val status: Status? = null
) 
{
    /**
    * pet status in the store
    * Values: available,pending,sold
    */
    enum class Status(val value: kotlin.String){
        available("available"),
        pending("pending"),
        sold("sold");
    }
}

