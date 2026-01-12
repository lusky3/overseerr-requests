package app.lusk.client.data.remote.model

import kotlinx.serialization.Serializable

/**
 * API model for request quota.
 */
@Serializable
data class ApiRequestQuota(
    val movie: ApiQuotaInfo?,
    val tv: ApiQuotaInfo?
)

/**
 * API model for quota information.
 */
@Serializable
data class ApiQuotaInfo(
    val limit: Int?,
    val remaining: Int?,
    val days: Int?
)
