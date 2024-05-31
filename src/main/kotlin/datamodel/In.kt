package datamodel

/**
 * https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.1.0.md#parameterIn
 */
enum class In(val value: String) {
    QUERY("query"),
    HEADER("header"),
    PATH("path"),
    COOKIE("cookie"),
//    FORM_DATA("formData"),
//    BODY("body");
    ;

    companion object {
        private val map = entries.associateBy(In::value)
        fun fromValue(value: String): In {
            return requireNotNull(map[value]) { "Camp In in the parameter section is required" }
        }
    }
}