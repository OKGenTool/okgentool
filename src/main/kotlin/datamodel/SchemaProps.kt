package datamodel

import generator.decapitalize
import io.swagger.v3.oas.models.media.Schema

enum class SchemaProps() {
    REF(),
    TYPE(),
    FORMAT(),
    ARRAYTYPE(),
    ARRAYFORMAT();

    companion object {
        fun getSchemaProp(schema: Schema<Any>?, prop: SchemaProps): String? =
            when (prop) {
                REF -> schema?.`$ref`
                TYPE -> schema?.type
                FORMAT -> schema?.format
                ARRAYTYPE -> schema?.items?.type
                ARRAYFORMAT -> schema?.items?.format
            }

        fun getRefSimpleName(ref: String): String =
            ref.split("/")
                .last()
                .decapitalize()
    }
}