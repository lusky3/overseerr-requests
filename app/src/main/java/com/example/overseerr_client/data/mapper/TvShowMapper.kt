package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.local.entity.TvShowEntity
import com.example.overseerr_client.data.remote.model.ApiTvShow
import com.example.overseerr_client.domain.model.TvShow
import com.example.overseerr_client.domain.model.MediaInfo
import com.example.overseerr_client.domain.model.MediaStatus

/**
 * Maps API TV show model to domain TV show model.
 */
fun ApiTvShow.toDomain(): TvShow {
    return TvShow(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        numberOfSeasons = numberOfSeasons,
        mediaInfo = mediaInfo?.toDomain()
    )
}

/**
 * Maps database TV show entity to domain TV show model.
 */
fun TvShowEntity.toDomain(): TvShow {
    return TvShow(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        numberOfSeasons = numberOfSeasons,
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
 * Maps domain TV show model to database entity.
 */
fun TvShow.toEntity(cachedAt: Long = System.currentTimeMillis()): TvShowEntity {
    return TvShowEntity(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        numberOfSeasons = numberOfSeasons,
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
