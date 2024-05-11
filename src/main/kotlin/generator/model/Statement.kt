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
        "require($parameterName == null || $parameterName %% $multipleOf == 0) { \"$parameterName must be a multiple of $multipleOf\" }" });
}