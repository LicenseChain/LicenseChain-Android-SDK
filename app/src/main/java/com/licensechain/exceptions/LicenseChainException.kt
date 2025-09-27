package com.licensechain.exceptions

class LicenseChainException(
    message: String,
    val errorCode: String = "UNKNOWN_ERROR",
    val statusCode: Int = 500,
    cause: Throwable? = null
) : Exception(message, cause)

class NetworkException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "NETWORK_ERROR", 0, cause)

class ValidationException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "VALIDATION_ERROR", 400, cause)

class AuthenticationException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "AUTHENTICATION_ERROR", 401, cause)

class NotFoundException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "NOT_FOUND_ERROR", 404, cause)

class RateLimitException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "RATE_LIMIT_ERROR", 429, cause)

class ServerException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "SERVER_ERROR", 500, cause)

class ConfigurationException(
    message: String,
    cause: Throwable? = null
) : LicenseChainException(message, "CONFIGURATION_ERROR", 500, cause)
