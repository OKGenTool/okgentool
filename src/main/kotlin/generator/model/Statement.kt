package generator.model

enum class Statement(val statement: (Any, Any) -> String) {
    MAXIMUM({ maximum, parameterName ->
        getRequireStatement(
            "$parameterName <= $maximum",
            "$parameterName must be less than or equal to $maximum"
        )}),

    MINIMUM({ minimum, parameterName ->
        getRequireStatement(
            "$parameterName >= $minimum",
            "$parameterName must be greater than or equal to $minimum"
        )}),

    MAXIMUM_NULLABLE({ maximum, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName <= $maximum",
            "$parameterName must be less than or equal to $maximum"
        )}),

    MINIMUM_NULLABLE({ minimum, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName >= $minimum",
            "$parameterName must be greater than or equal to $minimum"
        )}),

    EXCLUSIVE_MAXIMUM({ maximum, parameterName ->
        getRequireStatement(
            "$parameterName < $maximum",
            "$parameterName must be less than $maximum"
        )}),

    EXCLUSIVE_MINIMUM({ minimum, parameterName ->
        getRequireStatement(
            "$parameterName > $minimum",
            "$parameterName must be greater than $minimum"
        )}),

    EXCLUSIVE_MAXIMUM_NULLABLE({ maximum, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName < $maximum",
            "$parameterName must be less than $maximum"
        )}),

    EXCLUSIVE_MINIMUM_NULLABLE({ minimum, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName > $minimum",
            "$parameterName must be greater than $minimum"
        )}),

    MULTIPLE_OF({ multipleOf, parameterName ->
        getRequireStatement(
            "$parameterName %% $multipleOf == 0",
            "$parameterName must be a multiple of $multipleOf"
        )}),

    MULTIPLE_OF_NULLABLE({ multipleOf, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName %% $multipleOf == 0",
            "$parameterName must be a multiple of $multipleOf"
        )}),

    MIN_LENGTH({ minLength, parameterName ->
        getRequireStatement(
            "$parameterName.length >= $minLength",
            "$parameterName must have a minimum length of $minLength"
        )}),

    MAX_LENGTH({ maxLength, parameterName ->
        getRequireStatement(
            "$parameterName.length <= $maxLength",
            "$parameterName must have a maximum length of $maxLength"
        )}),

    MIN_LENGTH_NULLABLE({ minLength, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.length >= $minLength",
            "$parameterName must have a minimum length of $minLength"
        )}),

    MAX_LENGTH_NULLABLE({ maxLength, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.length <= $maxLength",
            "$parameterName must have a maximum length of $maxLength"
        )}),

    PATTERN({ pattern, parameterName ->
        getRequireStatement(
            "$parameterName.matches(\"$pattern\".toRegex())",
            "$parameterName must match the pattern $pattern"
        )}),

    PATTERN_NULLABLE({ pattern, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.matches(\"$pattern\".toRegex())",
            "$parameterName must match the pattern $pattern"
        )}),

    MIN_ITEMS({ minItems, parameterName ->
        getRequireStatement(
            "$parameterName.size >= $minItems",
            "$parameterName must have a minimum length of $minItems"
        )}),

    MIN_ITEMS_NULLABLE({ minItems, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.size >= $minItems",
            "$parameterName must have a minimum length of $minItems"
        )}),

    MAX_ITEMS({ maxItems, parameterName ->
        getRequireStatement(
            "$parameterName.size <= $maxItems",
            "$parameterName must have a maximum length of $maxItems"
        )}),

    MAX_ITEMS_NULLABLE({ maxItems, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.size <= $maxItems",
            "$parameterName must have a maximum length of $maxItems"
        )}),

    UNIQUE_ITEMS({ _, parameterName ->
        getRequireStatement(
            "$parameterName.toSet().size == $parameterName.size",
            "$parameterName must have unique items"
        )}),

    UNIQUE_ITEMS_NULLABLE({ _, parameterName ->
        getRequireStatement(
            "$parameterName == null || $parameterName.toSet().size == $parameterName.size",
            "$parameterName must have unique items"
        )})
}

private fun getRequireStatement(condition: String, errorMessage: String): String {
    val formattedCondition = condition
        .replace("||", "||\n   ")
        .replace("&&", "&&\n   ")

    val formattedErrorMessage = if (errorMessage.length > 86) {
        val parts = errorMessage.chunked(83)
        parts.joinToString("\" +\n    \"")
    } else {
        errorMessage
    }

    return """
        |    require(
        |    $formattedCondition
        |) {
        |    "$formattedErrorMessage"
        |}
    """.trimMargin()
}