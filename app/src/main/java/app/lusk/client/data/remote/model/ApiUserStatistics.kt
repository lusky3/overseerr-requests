package app.lusk.client.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API model for user statistics.
 */
@Serializable
data class ApiUserStatistics(
    @SerialName("total") val totalRequests: Int,
    @SerialName("approved") val approvedRequests: Int,
    @SerialName("declined") val declinedRequests: Int,
    @SerialName("pending") val pendingRequests: Int,
    @SerialName("available") val availableRequests: Int
)
