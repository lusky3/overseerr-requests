package app.lusk.client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for TV show data from Overseerr.
 */
@Serializable
data class ApiTvShow(
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("number_of_seasons") val numberOfSeasons: Int,
    @SerialName("media_info") val mediaInfo: ApiMediaInfo? = null
)
