package com.licensechain.utils

import android.util.Log
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Utils {
    
    // Validation functions
    fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        val pattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        return pattern.matcher(email).matches()
    }
    
    fun validateLicenseKey(licenseKey: String): Boolean {
        if (licenseKey.isEmpty() || licenseKey.length != 32) return false
        val pattern = Pattern.compile("^[A-Z0-9]+$")
        return pattern.matcher(licenseKey).matches()
    }
    
    fun validateUuid(uuid: String): Boolean {
        if (uuid.isEmpty()) return false
        val pattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
        return pattern.matcher(uuid).matches()
    }
    
    fun validateAmount(amount: Double): Boolean {
        return amount > 0 && amount != Double.MAX_VALUE && amount != Double.MIN_VALUE
    }
    
    fun validateCurrency(currency: String): Boolean {
        val validCurrencies = listOf("USD", "EUR", "GBP", "CAD", "AUD", "JPY", "CHF", "CNY")
        return validCurrencies.contains(currency)
    }
    
    // Input sanitization
    fun sanitizeInput(input: String): String {
        if (input.isEmpty()) return input
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
    }
    
    fun sanitizeMetadata(metadata: Map<String, Any>): Map<String, Any> {
        val sanitized = mutableMapOf<String, Any>()
        for ((key, value) in metadata) {
            when (value) {
                is String -> sanitized[key] = sanitizeInput(value)
                is Map<*, *> -> sanitized[key] = sanitizeMetadata(value as Map<String, Any>)
                is List<*> -> {
                    val sanitizedList = (value as List<Any>).map { item ->
                        if (item is String) sanitizeInput(item) else item
                    }
                    sanitized[key] = sanitizedList
                }
                else -> sanitized[key] = value
            }
        }
        return sanitized
    }
    
    // Generation functions
    fun generateLicenseKey(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = SecureRandom()
        return (1..32)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }
    
    fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }
    
    // String utilities
    fun capitalizeFirst(text: String): String {
        if (text.isEmpty()) return text
        return text[0].uppercaseChar() + text.substring(1).lowercase()
    }
    
    fun toSnakeCase(text: String): String {
        if (text.isEmpty()) return text
        return text.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
    }
    
    fun toPascalCase(text: String): String {
        if (text.isEmpty()) return text
        return text.split("_").joinToString("") { capitalizeFirst(it) }
    }
    
    fun truncateString(text: String, maxLength: Int): String {
        if (text.length <= maxLength) return text
        return text.substring(0, maxLength - 3) + "..."
    }
    
    fun slugify(text: String): String {
        if (text.isEmpty()) return text
        return text.lowercase()
            .replace(" ", "-")
            .replace("_", "-")
            .replace(Regex("-+"), "-")
            .trim('-')
    }
    
    // Formatting functions
    fun formatBytes(bytes: Long): String {
        val units = listOf("B", "KB", "MB", "GB", "TB", "PB")
        var size = bytes.toDouble()
        var unitIndex = 0
        
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        
        return "%.1f %s".format(size, units[unitIndex])
    }
    
    fun formatDuration(seconds: Int): String {
        return when {
            seconds < 60 -> "${seconds}s"
            seconds < 3600 -> {
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                "${minutes}m ${remainingSeconds}s"
            }
            seconds < 86400 -> {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                "${hours}h ${minutes}m"
            }
            else -> {
                val days = seconds / 86400
                val hours = (seconds % 86400) / 3600
                "${days}d ${hours}h"
            }
        }
    }
    
    fun formatPrice(price: Double, currency: String = "USD"): String {
        val symbol = when (currency) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            else -> currency
        }
        return "$symbol%.2f".format(price)
    }
    
    fun formatTimestamp(timestamp: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(timestamp)
    }
    
    fun parseTimestamp(timestamp: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(timestamp) ?: Date()
    }
    
    // Validation helpers
    fun validateNotEmpty(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw com.licensechain.exceptions.ValidationException("$fieldName cannot be empty")
        }
    }
    
    fun validatePositive(value: Double, fieldName: String) {
        if (value <= 0) {
            throw com.licensechain.exceptions.ValidationException("$fieldName must be positive")
        }
    }
    
    fun validateRange(value: Double, min: Double, max: Double, fieldName: String) {
        if (value < min || value > max) {
            throw com.licensechain.exceptions.ValidationException("$fieldName must be between $min and $max")
        }
    }
    
    // Pagination
    fun validatePagination(page: Int, limit: Int): Pair<Int, Int> {
        val validPage = maxOf(page, 1)
        val validLimit = minOf(maxOf(limit, 1), 100)
        return Pair(validPage, validLimit)
    }
    
    // Date range validation
    fun validateDateRange(startDate: Date, endDate: Date) {
        if (startDate.after(endDate)) {
            throw com.licensechain.exceptions.ValidationException("Start date must be before or equal to end date")
        }
    }
    
    // Crypto functions
    fun createWebhookSignature(payload: String, secret: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        mac.init(secretKeySpec)
        val hash = mac.doFinal(payload.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    fun verifyWebhookSignature(payload: String, signature: String, secret: String): Boolean {
        val expectedSignature = createWebhookSignature(payload, secret)
        return signature.equals(expectedSignature, ignoreCase = true)
    }
    
    fun sha256(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    fun sha1(data: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val hash = digest.digest(data.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    fun md5(data: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val hash = digest.digest(data.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    
    // Encoding/Decoding
    fun base64Encode(data: String): String {
        return Base64.getEncoder().encodeToString(data.toByteArray())
    }
    
    fun base64Decode(data: String): String {
        return String(Base64.getDecoder().decode(data))
    }
    
    fun urlEncode(data: String): String {
        return java.net.URLEncoder.encode(data, "UTF-8")
    }
    
    fun urlDecode(data: String): String {
        return java.net.URLDecoder.decode(data, "UTF-8")
    }
    
    // JSON utilities
    fun isValidJson(jsonString: String): Boolean {
        if (jsonString.isEmpty()) return false
        try {
            val gson = com.google.gson.Gson()
            gson.fromJson(jsonString, Any::class.java)
            return true
        } catch (e: Exception) {
            return false
        }
    }
    
    // URL utilities
    fun isValidUrl(urlString: String): Boolean {
        val pattern = Pattern.compile("^https?://[^\\s/$.?#].[^\\s]*$")
        return pattern.matcher(urlString).matches()
    }
    
    // Time utilities
    fun getCurrentTimestamp(): Date {
        return Date()
    }
    
    fun getCurrentDate(): String {
        return formatTimestamp(getCurrentTimestamp())
    }
    
    // Array utilities
    fun <T> chunkArray(array: List<T>, chunkSize: Int): List<List<T>> {
        return array.chunked(chunkSize)
    }
    
    // Object utilities
    fun deepMerge(target: Map<String, Any>, source: Map<String, Any>): Map<String, Any> {
        val result = target.toMutableMap()
        for ((key, value) in source) {
            result[key] = value
        }
        return result
    }
    
    fun flattenObject(obj: Map<String, Any>, separator: String = "."): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        flattenObjectRecursive(obj, "", separator, result)
        return result
    }
    
    private fun flattenObjectRecursive(obj: Any, prefix: String, separator: String, result: MutableMap<String, Any>) {
        when (obj) {
            is Map<*, *> -> {
                for ((key, value) in obj) {
                    val newKey = if (prefix.isEmpty()) key.toString() else "$prefix$separator$key"
                    flattenObjectRecursive(value!!, newKey, separator, result)
                }
            }
            is List<*> -> {
                for ((index, value) in obj.withIndex()) {
                    val newKey = if (prefix.isEmpty()) index.toString() else "$prefix$separator$index"
                    flattenObjectRecursive(value!!, newKey, separator, result)
                }
            }
            else -> result[prefix] = obj
        }
    }
    
    fun unflattenObject(obj: Map<String, Any>, separator: String = "."): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        for ((key, value) in obj) {
            val keys = key.split(separator)
            var current = result
            for (i in 0 until keys.size - 1) {
                val k = keys[i]
                if (!current.containsKey(k)) {
                    current[k] = mutableMapOf<String, Any>()
                }
                current = current[k] as MutableMap<String, Any>
            }
            current[keys.last()] = value
        }
        return result
    }
    
    // Random generation
    fun generateRandomString(length: Int, characters: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"): String {
        val random = SecureRandom()
        return (1..length)
            .map { characters[random.nextInt(characters.length)] }
            .joinToString("")
    }
    
    fun generateRandomBytes(length: Int): ByteArray {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return bytes
    }
    
    // Logging
    fun log(level: String, tag: String, message: String) {
        when (level.uppercase()) {
            "DEBUG" -> Log.d(tag, message)
            "INFO" -> Log.i(tag, message)
            "WARN" -> Log.w(tag, message)
            "ERROR" -> Log.e(tag, message)
            else -> Log.v(tag, message)
        }
    }
    
    // Retry logic
    inline fun <T> retryWithBackoff(
        maxRetries: Int = 3,
        initialDelayMs: Long = 1000,
        operation: () -> T
    ): T {
        var delay = initialDelayMs
        var lastException: Exception? = null
        
        repeat(maxRetries) { attempt ->
            try {
                return operation()
            } catch (e: Exception) {
                lastException = e
                if (attempt < maxRetries - 1) {
                    Thread.sleep(delay)
                    delay *= 2 // Exponential backoff
                }
            }
        }
        
        throw lastException ?: Exception("All retry attempts failed")
    }
}
