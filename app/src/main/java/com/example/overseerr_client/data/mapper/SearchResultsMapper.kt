package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.remote.model.ApiSearchResult
import com.example.overseerr_client.data.remote.model.ApiSearchResults
import com.example.overseerr_client.domain.model.MediaType
import com.example.overseerr_client.domain.model.SearchResult
import com.example.overseerr_client.domain.model.SearchResults

/**
 * Maps API search results model to domain search results model.
 */
fun ApiSearchResults.toDomain(): SearchResults {
    return SearchResults(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults,
        results = results.map { it.toDomain() }
    )
}

/**
 * Maps API search result model to domain search result model.
 */
fun ApiSearchResult.toDomain(): SearchResult {
    return SearchResult(
        id = id,
        mediaType = mediaType.toMediaType(),
        title = title ?: name ?: "",
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: firstAirDate,
        voteAverage = voteAverage
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
