package com.example.overseerr_client.navigation

/**
 * Sealed class representing all navigation destinations in the app.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.4, 6.4
 */
sealed class Screen(val route: String) {
    
    // Authentication
    data object ServerConfig : Screen("server_config")
    data object PlexAuth : Screen("plex_auth")
    
    // Main Navigation
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Requests : Screen("requests")
    data object Profile : Screen("profile")
    
    // Details
    data object MediaDetails : Screen("media_details/{mediaType}/{mediaId}") {
        fun createRoute(mediaType: String, mediaId: Int) = "media_details/$mediaType/$mediaId"
    }
    
    data object RequestDetails : Screen("request_details/{requestId}") {
        fun createRoute(requestId: Int) = "request_details/$requestId"
    }
    
    // Settings
    data object Settings : Screen("settings")
    data object ServerManagement : Screen("server_management")
    
    companion object {
        /**
         * Get screen from route string.
         */
        fun fromRoute(route: String?): Screen? {
            return when {
                route == null -> null
                route.startsWith("server_config") -> ServerConfig
                route.startsWith("plex_auth") -> PlexAuth
                route.startsWith("home") -> Home
                route.startsWith("search") -> Search
                route.startsWith("requests") -> Requests
                route.startsWith("profile") -> Profile
                route.startsWith("media_details") -> MediaDetails
                route.startsWith("request_details") -> RequestDetails
                route.startsWith("settings") -> Settings
                route.startsWith("server_management") -> ServerManagement
                else -> null
            }
        }
        
        /**
         * Parse deep link URI to screen route.
         */
        fun parseDeepLink(uri: String): String? {
            return when {
                uri.startsWith("overseerr://media/") -> {
                    val parts = uri.removePrefix("overseerr://media/").split("/")
                    if (parts.size == 2) {
                        MediaDetails.createRoute(parts[0], parts[1].toIntOrNull() ?: 0)
                    } else null
                }
                uri.startsWith("overseerr://request/") -> {
                    val requestId = uri.removePrefix("overseerr://request/").toIntOrNull()
                    requestId?.let { RequestDetails.createRoute(it) }
                }
                else -> null
            }
        }
    }
}
