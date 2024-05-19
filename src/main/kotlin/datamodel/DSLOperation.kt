package datamodel

data class DSLOperation(val name: String) {
//    var parameters: List<Parameter>? = null
    var requestBody: BodyNew? = null
//    var responses: List<Response>? = null

    constructor(
        name: String,
//        parameters: List<Parameter>,
        requestBody: BodyNew?,
//        responses: List<Response>,
    ) : this(name) {
//        this.parameters = parameters
        this.requestBody = requestBody
//        this.responses = responses
    }
}
