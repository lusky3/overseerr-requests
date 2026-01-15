package app.lusk.client.data.mapper

import app.lusk.client.data.remote.model.ApiServerInfo
import app.lusk.client.domain.model.ServerInfo

/**
 * Maps API server info model to domain server info model.
 */
fun ApiServerInfo.toDomain(): ServerInfo {
    return ServerInfo(
        version = version,
        initialized = initialized,
        applicationUrl = applicationUrl
    )
}
