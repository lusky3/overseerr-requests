package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for media request data.
 */
@Serializable
data class ApiMediaRequest(
    val id: Int,
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String? = null,
    val status: Int,
    @SerialName("created_at") val createdAt: String,
    val seasons: List<Int>? = null
)
