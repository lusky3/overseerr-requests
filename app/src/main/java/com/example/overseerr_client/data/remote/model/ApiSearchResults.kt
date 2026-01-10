package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for search results.
 */
@Serializable
data class ApiSearchResults(
    val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int,
    val results: List<ApiSearchResult>
)

@Serializable
data class ApiSearchResult(
    val id: Int,
    @SerialName("media_type") val mediaType: String,
    val title: String? = null,
    val name: String? = null,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double
)
