package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for server information.
 */
@Serializable
data class ApiServerInfo(
    val version: String,
    val initialized: Boolean,
    @SerialName("application_url") val applicationUrl: String
)
