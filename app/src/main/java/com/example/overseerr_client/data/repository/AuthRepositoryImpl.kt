package com.example.overseerr_client.data.repository

import com.example.overseerr_client.data.preferences.PreferencesManager
import com.example.overseerr_client.data.remote.api.AuthApiService
import com.example.overseerr_client.data.remote.api.PlexAuthRequest
import com.example.overseerr_client.data.remote.interceptor.AuthInterceptor
import com.example.overseerr_client.data.remote.model.toPermissions
import com.example.overseerr_client.data.remote.safeApiCall
import com.example.overseerr_client.domain.security.SecurityManager
import com.example.overseerr_client.domain.model.OverseerrSession
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.model.ServerInfo
import com.example.overseerr_client.domain.model.UserProfile
import com.example.overseerr_client.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthRepository for authentication operations.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.2, 1.3, 1.4, 1.5, 1.6
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val securityManager: SecurityManager,
    private val preferencesManager: PreferencesManager,
    private val authInterceptor: AuthInterceptor
) : AuthRepository {
    
    companion object {
        private const val API_KEY_STORAGE_KEY = "overseerr_api_key"
        private const val SESSION_STORAGE_KEY = "overseerr_session"
    }
    
    override suspend fun validateServerUrl(url: String): Result<ServerInfo> {
        return try {
            // Validate URL format
            if (!isValidUrl(url)) {
                return Result.error(
                    com.example.overseerr_client.domain.model.AppError.ValidationError(
                        "Invalid server URL format. Must be a valid HTTP/HTTPS URL."
                    )
                )
            }
            
            // Enforce HTTPS for security
            if (!url.startsWith("https://", ignoreCase = true)) {
                return Result.error(
                    com.example.overseerr_client.domain.model.AppError.ValidationError(
                        "Server URL must use HTTPS for security."
                    )
                )
            }
            
            // Store server URL
            preferencesManager.setServerUrl(url)
            
            // Try to fetch server info to validate connectivity
            val result = safeApiCall {
                authApiService.getServerInfo()
            }
            
            when (result) {
                is Result.Success -> {
                    val apiServerInfo = result.data
                    Result.success(
                        ServerInfo(
                            version = apiServerInfo.version,
                            initialized = apiServerInfo.initialized,
                            applicationUrl = apiServerInfo.applicationUrl
                        )
                    )
                }
                is Result.Error -> result
                is Result.Loading -> Result.loading()
            }
        } catch (e: Exception) {
            Result.error(
                com.example.overseerr_client.domain.model.AppError.NetworkError(
                    "Failed to connect to server: ${e.message}"
                )
            )
        }
    }
    
    override suspend fun authenticateWithPlex(plexToken: String): Result<UserProfile> {
        return try {
            // Call Plex authentication endpoint
            val result = safeApiCall {
                authApiService.authenticateWithPlex(PlexAuthRequest(plexToken))
            }
            
            when (result) {
                is Result.Success -> {
                    val apiUserProfile = result.data
                    
                    // Extract API key from user profile (if available in response)
                    // Note: Overseerr typically returns session cookie, but we'll handle API key
                    val apiKey = apiUserProfile.email // Placeholder - adjust based on actual API
                    
                    // Store API key securely
                    securityManager.storeSecureData(API_KEY_STORAGE_KEY, apiKey)
                    
                    // Update auth interceptor
                    authInterceptor.setApiKey(apiKey)
                    
                    // Store user ID
                    preferencesManager.setUserId(apiUserProfile.id)
                    
                    // Map to domain model
                    val userProfile = UserProfile(
                        id = apiUserProfile.id,
                        email = apiUserProfile.email,
                        displayName = apiUserProfile.displayName,
                        avatar = apiUserProfile.avatar,
                        requestCount = apiUserProfile.requestCount,
                        permissions = apiUserProfile.permissions.toPermissions()
                    )
                    
                    Result.success(userProfile)
                }
                is Result.Error -> result
                is Result.Loading -> Result.loading()
            }
        } catch (e: Exception) {
            Result.error(
                com.example.overseerr_client.domain.model.AppError.AuthError(
                    "Authentication failed: ${e.message}"
                )
            )
        }
    }
    
    override fun getStoredSession(): Flow<OverseerrSession?> {
        return preferencesManager.getUserId().map { userId ->
            if (userId != null) {
                val apiKey = securityManager.retrieveSecureData(API_KEY_STORAGE_KEY)
                val serverUrl = preferencesManager.getServerUrl().first()
                if (apiKey != null && serverUrl != null) {
                    OverseerrSession(
                        userId = userId,
                        apiKey = apiKey,
                        serverUrl = serverUrl,
                        expiresAt = null
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
    
    override suspend fun getCurrentUser(): Result<UserProfile> {
        return try {
            // Ensure we have an API key
            val apiKey = securityManager.retrieveSecureData(API_KEY_STORAGE_KEY)
            if (apiKey == null) {
                return Result.error(
                    com.example.overseerr_client.domain.model.AppError.AuthError(
                        "Not authenticated. Please log in."
                    )
                )
            }
            
            // Update auth interceptor
            authInterceptor.setApiKey(apiKey)
            
            // Fetch current user
            val result = safeApiCall {
                authApiService.getCurrentUser()
            }
            
            when (result) {
                is Result.Success -> {
                    val apiUserProfile = result.data
                    val userProfile = UserProfile(
                        id = apiUserProfile.id,
                        email = apiUserProfile.email,
                        displayName = apiUserProfile.displayName,
                        avatar = apiUserProfile.avatar,
                        requestCount = apiUserProfile.requestCount,
                        permissions = apiUserProfile.permissions.toPermissions()
                    )
                    Result.success(userProfile)
                }
                is Result.Error -> result
                is Result.Loading -> Result.loading()
            }
        } catch (e: Exception) {
            Result.error(
                com.example.overseerr_client.domain.model.AppError.AuthError(
                    "Failed to get current user: ${e.message}"
                )
            )
        }
    }
    
    override suspend fun logout() {
        try {
            // Call logout endpoint (best effort)
            safeApiCall {
                authApiService.logout()
            }
        } catch (e: Exception) {
            // Ignore errors during logout API call
        }
        
        // Clear stored credentials
        securityManager.clearSecureData()
        preferencesManager.clearAuthData()
        
        // Clear auth interceptor
        authInterceptor.clearApiKey()
    }
    
    override fun isAuthenticated(): Flow<Boolean> {
        return getStoredSession().map { it != null }
    }
    
    override suspend fun refreshSession(): Result<UserProfile> {
        // For now, just get current user
        // In a real implementation, this might refresh tokens
        return getCurrentUser()
    }
    
    /**
     * Validate URL format.
     */
    private fun isValidUrl(url: String): Boolean {
        return try {
            val parsedUrl = URL(url)
            val scheme = parsedUrl.protocol
            val host = parsedUrl.host
            
            // Must have http or https scheme
            if (scheme != "http" && scheme != "https") {
                return false
            }
            
            // Must have a host
            if (host.isNullOrBlank()) {
                return false
            }
            
            true
        } catch (e: Exception) {
            false
        }
    }
}
