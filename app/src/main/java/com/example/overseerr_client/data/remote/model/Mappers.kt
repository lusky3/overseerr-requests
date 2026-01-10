package com.example.overseerr_client.data.remote.model

import com.example.overseerr_client.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mapper functions to convert API models to domain models.
 */

fun ApiMovie.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        mediaInfo = mediaInfo?.toMediaInfo()
    )
}

fun ApiTvShow.toTvShow(): TvShow {
    return TvShow(
        id = id,
        name = name,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        voteAverage = voteAverage,
        numberOfSeasons = numberOfSeasons,
        mediaInfo = mediaInfo?.toMediaInfo()
    )
}

fun ApiSearchResults.toSearchResults(): SearchResults {
    return SearchResults(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults,
        results = results.map { it.toSearchResult() }
    )
}

fun ApiSearchResult.toSearchResult(): SearchResult {
    return SearchResult(
        id = id,
        mediaType = when (mediaType.lowercase()) {
            "movie" -> MediaType.MOVIE
            "tv" -> MediaType.TV
            else -> MediaType.MOVIE
        },
        title = title ?: name ?: "",
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: firstAirDate,
        voteAverage = voteAverage
    )
}

fun ApiMediaRequest.toMediaRequest(): MediaRequest {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    
    val requestedDate = try {
        dateFormat.parse(createdAt)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
    
    return MediaRequest(
        id = id,
        mediaType = when (mediaType.lowercase()) {
            "movie" -> MediaType.MOVIE
            "tv" -> MediaType.TV
            else -> MediaType.MOVIE
        },
        mediaId = mediaId,
        title = title,
        posterPath = posterPath,
        status = when (status) {
            1 -> RequestStatus.PENDING
            2 -> RequestStatus.APPROVED
            3 -> RequestStatus.DECLINED
            4 -> RequestStatus.AVAILABLE
            else -> RequestStatus.PENDING
        },
        requestedDate = requestedDate,
        seasons = seasons
    )
}

fun ApiMediaInfo.toMediaInfo(): MediaInfo {
    return MediaInfo(
        status = when (status) {
            1 -> MediaStatus.UNKNOWN
            2 -> MediaStatus.PENDING
            3 -> MediaStatus.PROCESSING
            4 -> MediaStatus.PARTIALLY_AVAILABLE
            5 -> MediaStatus.AVAILABLE
            else -> MediaStatus.UNKNOWN
        },
        requestId = requestId,
        available = available
    )
}

fun ApiPermissions.toPermissions(): Permissions {
    return Permissions(
        canRequest = canRequest,
        canManageRequests = canManageRequests,
        canViewRequests = canViewRequests,
        isAdmin = isAdmin
    )
}


fun ApiSearchResult.toMovie(): Movie {
    return Movie(
        id = id,
        title = title ?: name ?: "",
        overview = overview,
        posterPath = posterPath,
        backdropPath = null,
        releaseDate = releaseDate ?: firstAirDate,
        voteAverage = voteAverage,
        mediaInfo = null
    )
}

fun ApiSearchResult.toTvShow(): TvShow {
    return TvShow(
        id = id,
        name = name ?: title ?: "",
        overview = overview,
        posterPath = posterPath,
        backdropPath = null,
        firstAirDate = firstAirDate ?: releaseDate,
        voteAverage = voteAverage,
        numberOfSeasons = 0, // Not available in search result
        mediaInfo = null
    )
}
