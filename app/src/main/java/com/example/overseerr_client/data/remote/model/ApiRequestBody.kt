package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for submitting a media request.
 */
@Serializable
data class ApiRequestBody(
    @SerialName("media_id") val mediaId: Int,
    @SerialName("media_type") val mediaType: String,
    val seasons: List<Int>? = null,
    @SerialName("is4k") val is4k: Boolean = false,
    @SerialName("serverId") val serverId: Int? = null,
    @SerialName("profileId") val profileId: Int? = null,
    @SerialName("rootFolder") val rootFolder: String? = null
)
