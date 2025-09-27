package com.licensechain.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Webhook(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("events")
    val events: List<String>,
    
    @SerializedName("secret")
    val secret: String?,
    
    @SerializedName("created_at")
    val createdAt: Date,
    
    @SerializedName("updated_at")
    val updatedAt: Date
)

data class CreateWebhookRequest(
    @SerializedName("url")
    val url: String,
    
    @SerializedName("events")
    val events: List<String>,
    
    @SerializedName("secret")
    val secret: String?
)

data class UpdateWebhookRequest(
    @SerializedName("url")
    val url: String?,
    
    @SerializedName("events")
    val events: List<String>?,
    
    @SerializedName("secret")
    val secret: String?
)

data class WebhookListResponse(
    @SerializedName("data")
    val data: List<Webhook>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("limit")
    val limit: Int
)

data class WebhookEvent(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("data")
    val data: Any,
    
    @SerializedName("timestamp")
    val timestamp: Date,
    
    @SerializedName("signature")
    val signature: String
)
