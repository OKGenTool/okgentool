package datamodel

import io.swagger.v3.oas.models.media.Schema

data class InlineSchema(val name: String, val schema: Schema<Any>)