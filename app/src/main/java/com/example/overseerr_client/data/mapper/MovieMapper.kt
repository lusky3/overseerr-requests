package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.local.entity.MovieEntity
import com.example.overseerr_client.data.remote.model.ApiMovie
import com.example.overseerr_client.domain.model.Movie
import com.example.overseerr_client.domain.model.MediaInfo
import com.example.overseerr_client.domain.model.MediaStatus

/**
 * Maps API movie model to domain movie model.
 */
fun ApiMovie.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaInfo = mediaInfo?.toDomain()
    )
}

/**
 * Maps database movie entity to domain movie model.
 */
fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaInfo = if (mediaStatus != null) {
            MediaInfo(
                status = mediaStatus.toMediaStatus(),
                requestId = requestId,
                available = available
            )
        } else null
    )
}

/**
 * Maps domain movie model to database entity.
 */
fun Movie.toEntity(cachedAt: Long = System.currentTimeMillis()): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaStatus = mediaInfo?.status?.ordinal,
        requestId = mediaInfo?.requestId,
        available = mediaInfo?.available ?: false,
        cachedAt = cachedAt
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
