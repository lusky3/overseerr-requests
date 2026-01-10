package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.remote.model.ApiMediaInfo
import com.example.overseerr_client.domain.model.MediaInfo
import com.example.overseerr_client.domain.model.MediaStatus

/**
 * Maps API media info model to domain media info model.
 */
fun ApiMediaInfo.toDomain(): MediaInfo {
    return MediaInfo(
        status = status.toMediaStatus(),
        requestId = requestId,
        available = available
    )
}

/**
 * Converts integer status code to MediaStatus enum.
 */
private fun Int.toMediaStatus(): MediaStatus {
    return when (this) {
        1 -> MediaStatus.AVAILABLE
        2 -> MediaStatus.PENDING
        3 -> MediaStatus.PROCESSING
        4 -> MediaStatus.PARTIALLY_AVAILABLE
        else -> MediaStatus.UNKNOWN
    }
}
