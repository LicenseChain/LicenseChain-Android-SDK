package com.licensechain.services

import com.licensechain.exceptions.*
import com.licensechain.models.*
import com.licensechain.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class LicenseService(
    private val apiKey: String,
    private val baseUrl: String,
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
) {
    
    private val gson = Gson()
    
    suspend fun createLicense(request: CreateLicenseRequest): License = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(request.userId, "userId")
        Utils.validateNotEmpty(request.productId, "productId")
        
        val sanitizedRequest = request.copy(
            metadata = Utils.sanitizeMetadata(request.metadata)
        )
        
        val json = gson.toJson(sanitizedRequest)
        val requestBody = json.toRequestBody("application/json".toMediaType())
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                return@withContext apiResponse.data
            }
            
            throw LicenseChainException("Failed to create license: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while creating license", e)
        }
    }
    
    suspend fun getLicense(licenseId: String): License = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(licenseId, "licenseId")
        Utils.validateUuid(licenseId)
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses/$licenseId")
            .get()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                return@withContext apiResponse.data
            }
            
            if (response.code == 404) {
                throw NotFoundException("License with ID $licenseId not found")
            }
            
            throw LicenseChainException("Failed to get license: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while getting license", e)
        }
    }
    
    suspend fun updateLicense(licenseId: String, request: UpdateLicenseRequest): License = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(licenseId, "licenseId")
        Utils.validateUuid(licenseId)
        
        val sanitizedRequest = request.copy(
            metadata = Utils.sanitizeMetadata(request.metadata)
        )
        
        val json = gson.toJson(sanitizedRequest)
        val requestBody = json.toRequestBody("application/json".toMediaType())
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses/$licenseId")
            .put(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                return@withContext apiResponse.data
            }
            
            if (response.code == 404) {
                throw NotFoundException("License with ID $licenseId not found")
            }
            
            throw LicenseChainException("Failed to update license: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while updating license", e)
        }
    }
    
    suspend fun revokeLicense(licenseId: String): Unit = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(licenseId, "licenseId")
        Utils.validateUuid(licenseId)
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses/$licenseId")
            .delete()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            
            if (!response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                
                if (response.code == 404) {
                    throw NotFoundException("License with ID $licenseId not found")
                }
                
                throw LicenseChainException("Failed to revoke license: $responseBody")
                    .apply { statusCode = response.code }
            }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while revoking license", e)
        }
    }
    
    suspend fun validateLicense(licenseKey: String): Boolean = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(licenseKey, "licenseKey")
        
        // Use /licenses/verify endpoint with 'key' parameter to match API
        val request = mapOf("key" to licenseKey)
        val json = gson.toJson(request)
        val requestBody = json.toRequestBody("application/json".toMediaType())
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses/verify")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val validationResponse = gson.fromJson(responseBody, ValidationResponse::class.java)
                return@withContext validationResponse.valid
            }
            
            throw LicenseChainException("Failed to validate license: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while validating license", e)
        }
    }
    
    suspend fun listUserLicenses(userId: String, page: Int = 1, limit: Int = 10): LicenseListResponse = withContext(Dispatchers.IO) {
        Utils.validateNotEmpty(userId, "userId")
        Utils.validateUuid(userId)
        
        val (validPage, validLimit) = Utils.validatePagination(page, limit)
        
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses?user_id=$userId&page=$validPage&limit=$validLimit")
            .get()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                return@withContext gson.fromJson(responseBody, LicenseListResponse::class.java)
            }
            
            throw LicenseChainException("Failed to list user licenses: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while listing user licenses", e)
        }
    }
    
    suspend fun getLicenseStats(): LicenseStats = withContext(Dispatchers.IO) {
        val httpRequest = Request.Builder()
            .url("$baseUrl/licenses/stats")
            .get()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        
        try {
            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                return@withContext apiResponse.data
            }
            
            throw LicenseChainException("Failed to get license stats: $responseBody")
                .apply { statusCode = response.code }
        } catch (e: IOException) {
            throw NetworkException("Network error occurred while getting license stats", e)
        }
    }
    
    private data class ApiResponse<T>(val data: T)
    private data class ValidationResponse(val valid: Boolean)
}
