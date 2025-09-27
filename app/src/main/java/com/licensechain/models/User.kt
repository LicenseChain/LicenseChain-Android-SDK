package com.licensechain.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("created_at")
    val createdAt: Date,
    
    @SerializedName("updated_at")
    val updatedAt: Date,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class CreateUserRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class UpdateUserRequest(
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("name")
    val name: String?,
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap()
)

data class UserListResponse(
    @SerializedName("data")
    val data: List<User>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("limit")
    val limit: Int
)

data class UserStats(
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("active")
    val active: Int,
    
    @SerializedName("inactive")
    val inactive: Int
)
