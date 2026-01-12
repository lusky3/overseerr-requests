package app.lusk.client.domain.model

/**
 * Represents request quotas for movies and TV shows.
 */
data class RequestQuota(
    val movie: QuotaInfo,
    val tv: QuotaInfo
)
