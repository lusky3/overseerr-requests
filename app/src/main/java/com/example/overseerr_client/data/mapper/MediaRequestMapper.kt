package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.local.entity.MediaRequestEntity
import com.example.overseerr_client.data.remote.model.ApiMediaRequest
import com.example.overseerr_client.domain.model.MediaRequest
import com.example.overseerr_client.domain.model.MediaType
import com.example.overseerr_client.domain.model.RequestStatus
import java.text.SimpleDateFormat
import java.util.*

/**
 * Maps API media request model to domain media request model.
 */
fun ApiMediaRequest.toDomain(): MediaRequest {
    return MediaRequest(
        id = id,
        mediaType = mediaType.toMediaType(),
        mediaId = mediaId,
        title = title,
        posterPath = posterPath,
        status = status.toRequestStatus(),
        requestedDate = createdAt.toTimestamp(),
        seasons = seasons
    )
}

/**
 * Maps database media request entity to domain media request model.
 */
fun MediaRequestEntity.toDomain(): MediaRequest {
    return MediaRequest(
        id = id,
        mediaType = mediaType.toMediaType(),
        mediaId = mediaId,
        title = title,
        posterPath = posterPath,
        status = status.toRequestStatus(),
        requestedDate = requestedDate,
        seasons = seasons
    )
}

/**
 * Maps domain media request model to database entity.
 */
fun MediaRequest.toEntity(cachedAt: Long = System.currentTimeMillis()): MediaRequestEntity {
    return MediaRequestEntity(
        id = id,
        mediaType = mediaType.name.lowercase(),
        mediaId = mediaId,
        title = title,
        posterPath = posterPath,
        status = status.ordinal,
        requestedDate = requestedDate,
        seasons = seasons,
        cachedAt = cachedAt
    )
}

/**
 * Converts string media type to MediaType enum.
 */
private fun String.toMediaType(): MediaType {
    return when (this.lowercase()) {
        "movie" -> MediaType.MOVIE
        "tv" -> MediaType.TV
        else -> MediaType.MOVIE
    }
}

/**
 * Converts integer status code to RequestStatus enum.
 */
private fun Int.toRequestStatus(): RequestStatus {
    return when (this) {
        1 -> RequestStatus.PENDING
        2 -> RequestStatus.APPROVED
        3 -> RequestStatus.DECLINED
        4 -> RequestStatus.AVAILABLE
        else -> RequestStatus.PENDING
    }
}

/**
 * Converts ISO 8601 date string to timestamp.
 */
private fun String.toTimestamp(): Long {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        format.parse(this)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}
