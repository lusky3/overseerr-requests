package app.lusk.client.data.remote.model

import kotlinx.serialization.Serializable

/**
 * API model for request submission response.
 */
@Serializable
data class ApiRequestResponse(
    val id: Int,
    val status: Int,
    val media: ApiMediaInfo
)
