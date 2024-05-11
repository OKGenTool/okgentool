package datamodel

data class DSLOperation(val name: String) {
    var parameters: List<Parameter>? = null
    var requestBody: Body? = null
    var responses: List<Response>? = null

    constructor(
        name: String,
        parameters: List<Parameter>,
        requestBody: Body?,
        responses: List<Response>,
    ) : this(name) {
        this.parameters = parameters
        this.requestBody = requestBody
        this.responses = responses
    }
}
