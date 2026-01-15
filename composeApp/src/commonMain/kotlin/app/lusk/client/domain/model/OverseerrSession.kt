package app.lusk.client.domain.model

/**
 * Represents an authenticated session with Overseerr.
 */
data class OverseerrSession(
    val apiKey: String,
    val userId: Int,
    val serverUrl: String,
    val expiresAt: Long?
)
