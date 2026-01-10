package com.example.overseerr_client.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that adds authentication headers to API requests.
 * Feature: overseerr-android-client
 * Validates: Requirements 1.4, 8.2
 */
@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    
    @Volatile
    private var apiKey: String? = null
    
    /**
     * Set the API key to be used for authentication.
     * 
     * @param key The API key or session token
     */
    fun setApiKey(key: String?) {
        this.apiKey = key
    }
    
    /**
     * Clear the stored API key.
     */
    fun clearApiKey() {
        this.apiKey = null
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // If no API key is set, proceed without authentication header
        val apiKey = this.apiKey ?: return chain.proceed(originalRequest)
        
        // Add API key to request headers
        val authenticatedRequest = originalRequest.newBuilder()
            .header("X-Api-Key", apiKey)
            .header("Accept", "application/json")
            .build()
        
        return chain.proceed(authenticatedRequest)
    }
}
