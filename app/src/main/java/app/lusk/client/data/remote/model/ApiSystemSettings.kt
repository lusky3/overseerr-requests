package app.lusk.client.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiSystemSettings(
    val partialRequestsEnabled: Boolean = false
)
