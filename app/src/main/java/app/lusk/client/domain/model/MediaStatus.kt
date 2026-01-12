package app.lusk.client.domain.model

/**
 * Represents the availability status of media content.
 */
enum class MediaStatus {
    AVAILABLE,
    PENDING,
    PROCESSING,
    PARTIALLY_AVAILABLE,
    UNKNOWN
}
