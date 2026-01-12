package app.lusk.client.data.remote.api

import app.lusk.client.data.remote.model.ApiRequestQuota
import app.lusk.client.data.remote.model.ApiUserProfile
import app.lusk.client.data.remote.model.ApiUserStatistics
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for user profile endpoints.
 * Feature: overseerr-android-client
 * Validates: Requirements 5.1, 5.2, 5.3, 5.4, 5.5, 5.6
 */
interface UserApiService {
    
    /**
     * Get user profile by ID.
     * 
     * @param userId The ID of the user
     * @return User profile information
     */
    @GET("/api/v1/user/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): ApiUserProfile
    
    /**
     * Get current user's profile (same as /auth/me).
     * 
     * @return Current user profile
     */
    @GET("/api/v1/user")
    suspend fun getCurrentUser(): ApiUserProfile
    
    /**
     * Get user's quota information.
     * 
     * @return User quota details
     */
    @GET("/api/v1/user/quota")
    suspend fun getUserQuota(): ApiRequestQuota
    
    /**
     * Get user's statistics.
     * 
     * @return User statistics
     */
    @GET("/api/v1/user/stats")
    suspend fun getUserStatistics(): ApiUserStatistics
}
