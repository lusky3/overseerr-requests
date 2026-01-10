package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for user profile data.
 */
@Serializable
data class ApiUserProfile(
    val id: Int,
    val email: String,
    @SerialName("display_name") val displayName: String,
    val avatar: String? = null,
    @SerialName("request_count") val requestCount: Int,
    val permissions: ApiPermissions
)

@Serializable
data class ApiPermissions(
    @SerialName("can_request") val canRequest: Boolean,
    @SerialName("can_manage_requests") val canManageRequests: Boolean,
    @SerialName("can_view_requests") val canViewRequests: Boolean,
    @SerialName("is_admin") val isAdmin: Boolean
)
