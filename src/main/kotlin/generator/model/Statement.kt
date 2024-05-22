package generator.model

enum class Statement(val statement: (Any, Any) -> String) {
    MAXIMUM({ maximum, parameterName ->
        "require($parameterName <= $maximum) { \"$parameterName must be less than or equal to $maximum\" }" }),

    MINIMUM({ minimum, parameterName ->
        "require($parameterName >= $minimum) { \"$parameterName must be greater than or equal to $minimum\" }" }),

    MAXIMUM_NULLABLE({ maximum, parameterName ->
        "require($parameterName == null || $parameterName <= $maximum) { \"$parameterName must be less than or equal to $maximum\" }" }),

    MINIMUM_NULLABLE({ minimum, parameterName ->
        "require($parameterName == null || $parameterName >= $minimum) { \"$parameterName must be greater than or equal to $minimum\" }" }),

    EXCLUSIVE_MAXIMUM({ maximum, parameterName ->
        "require($parameterName < $maximum) { \"$parameterName must be less than $maximum\" }" }),

    EXCLUSIVE_MINIMUM({ minimum, parameterName ->
        "require($parameterName > $minimum) { \"$parameterName must be greater than $minimum\" }" }),

    EXCLUSIVE_MAXIMUM_NULLABLE({ maximum, parameterName ->
        "require($parameterName == null || $parameterName < $maximum) { \"$parameterName must be less than $maximum\" }" }),

    EXCLUSIVE_MINIMUM_NULLABLE({ minimum, parameterName ->
        "require($parameterName == null || $parameterName > $minimum) { \"$parameterName must be greater than $minimum\" }" }),

    MULTIPLE_OF({ multipleOf, parameterName ->
        "require($parameterName %% $multipleOf == 0) { \"$parameterName must be a multiple of $multipleOf\" }" }),

    MULTIPLE_OF_NULLABLE({ multipleOf, parameterName ->
        "require($parameterName == null || $parameterName %% $multipleOf == 0) { \"$parameterName must be a multiple of $multipleOf\" }" }),

    MIN_LENGTH({ minLength, parameterName ->
        "require($parameterName.length >= $minLength) { \"$parameterName must have a minimum length of $minLength\" }" }),

    MAX_LENGTH({ maxLength, parameterName ->
        "require($parameterName.length <= $maxLength) { \"$parameterName must have a maximum length of $maxLength\" }" }),

    MIN_LENGTH_NULLABLE({ minLength, parameterName ->
        "require($parameterName == null || $parameterName.length >= $minLength) { \"$parameterName must have a minimum length of $minLength\" }" }),

    MAX_LENGTH_NULLABLE({ maxLength, parameterName ->
        "require($parameterName == null || $parameterName.length <= $maxLength) { \"$parameterName must have a maximum length of $maxLength\" }" }),

    PATTERN({ pattern, parameterName ->
        "require($parameterName.matches(\"$pattern\".toRegex())) { \"$parameterName must match the pattern $pattern\" }" }),

    PATTERN_NULLABLE({ pattern, parameterName ->
        "require($parameterName == null || $parameterName.matches(\"$pattern\".toRegex())) { \"$parameterName must match the pattern $pattern\" }" }),

    MIN_ITEMS({ minItems, parameterName ->
        "require($parameterName.size >= $minItems) { \"$parameterName must have a minimum length of $minItems\" }" }),

    MIN_ITEMS_NULLABLE({ minItems, parameterName ->
        "require($parameterName == null || $parameterName.size >= $minItems) { \"$parameterName must have a minimum length of $minItems\" }" }),

    MAX_ITEMS({ maxItems, parameterName ->
        "require($parameterName.size <= $maxItems) { \"$parameterName must have a maximum length of $maxItems\" }" }),

    MAX_ITEMS_NULLABLE({ maxItems, parameterName ->
        "require($parameterName == null || $parameterName.size <= $maxItems) { \"$parameterName must have a maximum length of $maxItems\" }" }),

    UNIQUE_ITEMS({ _, parameterName ->
        "require($parameterName.toSet().size == $parameterName.size) { \"$parameterName must have unique items\" }" }),

    UNIQUE_ITEMS_NULLABLE({ _, parameterName ->
        "require($parameterName == null || $parameterName.toSet().size == $parameterName.size) { \"$parameterName must have unique items\" }" })
}