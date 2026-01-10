package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.remote.model.ApiServerInfo
import com.example.overseerr_client.domain.model.ServerInfo

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
