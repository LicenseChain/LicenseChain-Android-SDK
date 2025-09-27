package com.licensechain.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Product(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("currency")
    val currency: String,
    
    @SerializedName("created_at")
    val createdAt: Date,
    
    @SerializedName("updated_at")
    val updatedAt: Date,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class CreateProductRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("currency")
    val currency: String,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class UpdateProductRequest(
    @SerializedName("name")
    val name: String?,
    
    @SerializedName("description")
    val description: String?,
    
    @SerializedName("price")
    val price: Double?,
    
    @SerializedName("currency")
    val currency: String?,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class ProductListResponse(
    @SerializedName("data")
    val data: List<Product>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("limit")
    val limit: Int
)

data class ProductStats(
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("active")
    val active: Int,
    
    @SerializedName("revenue")
    val revenue: Double
)
