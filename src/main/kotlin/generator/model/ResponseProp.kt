package generator.model

import com.squareup.kotlinpoet.PropertySpec
import datamodel.ResponseNew

data class ResponseProp(val response: ResponseNew, val responseType: PropertySpec)
