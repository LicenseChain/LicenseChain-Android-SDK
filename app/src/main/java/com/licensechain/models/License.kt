package com.licensechain.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class License(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("user_id")
    val userId: String,
    
    @SerializedName("product_id")
    val productId: String,
    
    @SerializedName("license_key")
    val licenseKey: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("created_at")
    val createdAt: Date,
    
    @SerializedName("updated_at")
    val updatedAt: Date,
    
    @SerializedName("expires_at")
    val expiresAt: Date?,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class CreateLicenseRequest(
    @SerializedName("user_id")
    val userId: String,
    
    @SerializedName("product_id")
    val productId: String,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class UpdateLicenseRequest(
    @SerializedName("status")
    val status: String?,
    
    @SerializedName("expires_at")
    val expiresAt: Date?,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class LicenseListResponse(
    @SerializedName("data")
    val data: List<License>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("limit")
    val limit: Int
)

data class LicenseStats(
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("active")
    val active: Int,
    
    @SerializedName("expired")
    val expired: Int,
    
    @SerializedName("revoked")
    val revoked: Int,
    
    @SerializedName("revenue")
    val revenue: Double
)
