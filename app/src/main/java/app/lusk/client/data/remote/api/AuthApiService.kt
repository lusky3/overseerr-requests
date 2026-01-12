package app.lusk.client.data.remote.api

import app.lusk.client.data.remote.model.ApiAuthResponse
import app.lusk.client.data.remote.model.ApiServerInfo
import app.lusk.client.data.remote.model.ApiUserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit service interface for authentication endpoints.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.2, 1.3, 1.4, 1.5, 1.6
 */
interface AuthApiService {
    
    /**
     * Authenticate with Plex token and get Overseerr session.
     * 
     * @param body Request body containing Plex auth token
     * @return User profile with session information
     */
    @POST("/api/v1/auth/plex")
    suspend fun authenticateWithPlex(
        @Body body: PlexAuthRequest
    ): ApiUserProfile
    
    /**
     * Get currently authenticated user information.
     * 
     * @return Current user profile
     */
    @GET("/api/v1/auth/me")
    suspend fun getCurrentUser(): ApiUserProfile
    
    /**
     * Logout and clear session.
     */
    @POST("/api/v1/auth/logout")
    suspend fun logout()
    
    /**
     * Get server information and status.
     * 
     * @return Server information including version and status
     */
    @GET("/api/v1/status")
    suspend fun getServerInfo(): ApiServerInfo
}

/**
 * Request body for Plex authentication.
 */
@kotlinx.serialization.Serializable
data class PlexAuthRequest(
    @kotlinx.serialization.SerialName("authToken")
    val authToken: String
)
