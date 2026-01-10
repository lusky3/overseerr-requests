package com.example.overseerr_client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for movie data from Overseerr.
 */
@Serializable
data class ApiMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("media_info") val mediaInfo: ApiMediaInfo? = null
)
