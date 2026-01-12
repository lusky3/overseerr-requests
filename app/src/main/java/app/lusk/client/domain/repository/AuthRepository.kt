package app.lusk.client.domain.repository

import app.lusk.client.domain.model.OverseerrSession
import app.lusk.client.domain.model.Result
import app.lusk.client.domain.model.ServerInfo
import app.lusk.client.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.2, 1.3, 1.4, 1.5, 1.6
 */
interface AuthRepository {
    
    /**
     * Validate server URL and check connectivity.
     * Property 1: URL Validation Correctness
     */
    suspend fun validateServerUrl(url: String): Result<ServerInfo>
    
    /**
     * Authenticate with Plex token and exchange for Overseerr session.
     * Property 2: Token Exchange Integrity
     */
    suspend fun authenticateWithPlex(plexToken: String): Result<UserProfile>
    
    /**
     * Get stored session as a Flow.
     */
    fun getStoredSession(): Flow<OverseerrSession?>
    
    /**
     * Get current authenticated user.
     */
    suspend fun getCurrentUser(): Result<UserProfile>
    
    /**
     * Clear session and logout.
     * Property 21: Logout Cleanup
     */
    suspend fun logout()
    
    /**
     * Check if user is authenticated.
     */
    fun isAuthenticated(): Flow<Boolean>
    
    /**
     * Refresh session if needed.
     */
    suspend fun refreshSession(): Result<UserProfile>
}
