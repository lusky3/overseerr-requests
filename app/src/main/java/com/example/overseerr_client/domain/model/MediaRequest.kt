package com.example.overseerr_client.domain.model

/**
 * Domain model representing a media request.
 */
data class MediaRequest(
    val id: Int,
    val mediaType: MediaType,
    val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val status: RequestStatus,
    val requestedDate: Long,
    val seasons: List<Int>?
)
