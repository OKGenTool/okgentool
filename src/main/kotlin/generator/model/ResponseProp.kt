package generator.model

import com.squareup.kotlinpoet.PropertySpec
import datamodel.Response

data class ResponseProp(val response: Response, val responseType: PropertySpec)
