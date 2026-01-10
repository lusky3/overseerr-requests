package com.example.overseerr_client.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

/**
 * Interceptor that implements exponential backoff retry logic for failed requests.
 * Feature: overseerr-android-client, Property 38: Exponential Backoff Retry
 * Validates: Requirements 10.4
 */
@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {
    
    companion object {
        private const val MAX_RETRIES = 3
        private const val INITIAL_BACKOFF_MS = 1000L // 1 second
        private const val MAX_BACKOFF_MS = 10000L // 10 seconds
        private const val BACKOFF_MULTIPLIER = 2.0
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var attempt = 0
        
        while (attempt < MAX_RETRIES) {
            try {
                // Clear previous response if any
                response?.close()
                response = null
                exception = null
                
                // Attempt the request
                response = chain.proceed(request)
                
                // If successful or non-retryable error, return response
                if (response.isSuccessful || !isRetryableStatusCode(response.code)) {
                    return response
                }
                
                // Close the response before retrying
                response.close()
                
            } catch (e: IOException) {
                exception = e
                
                // If not retryable, throw immediately
                if (!isRetryableException(e)) {
                    throw e
                }
            }
            
            attempt++
            
            // If we've exhausted retries, throw exception or return last response
            if (attempt >= MAX_RETRIES) {
                exception?.let { throw it }
                response?.let { return it }
            }
            
            // Calculate backoff delay with exponential increase
            val backoffDelay = calculateBackoffDelay(attempt)
            
            try {
                Thread.sleep(backoffDelay)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                exception?.let { throw it }
                response?.let { return it }
                throw IOException("Retry interrupted", e)
            }
        }
        
        // This should never be reached, but handle it just in case
        exception?.let { throw it }
        return response ?: throw IOException("Request failed after $MAX_RETRIES retries")
    }
    
    /**
     * Calculate backoff delay using exponential backoff algorithm.
     * 
     * @param attempt The current attempt number (1-based)
     * @return Delay in milliseconds
     */
    private fun calculateBackoffDelay(attempt: Int): Long {
        val exponentialDelay = (INITIAL_BACKOFF_MS * BACKOFF_MULTIPLIER.pow(attempt - 1)).toLong()
        return min(exponentialDelay, MAX_BACKOFF_MS)
    }
    
    /**
     * Check if the HTTP status code is retryable.
     * 
     * @param code HTTP status code
     * @return true if the status code indicates a retryable error
     */
    private fun isRetryableStatusCode(code: Int): Boolean {
        return when (code) {
            408, // Request Timeout
            429, // Too Many Requests
            500, // Internal Server Error
            502, // Bad Gateway
            503, // Service Unavailable
            504  // Gateway Timeout
            -> true
            else -> false
        }
    }
    
    /**
     * Check if the exception is retryable.
     * 
     * @param exception The exception to check
     * @return true if the exception indicates a retryable error
     */
    private fun isRetryableException(exception: IOException): Boolean {
        return when (exception) {
            is SocketTimeoutException -> true
            else -> exception.message?.contains("timeout", ignoreCase = true) == true ||
                    exception.message?.contains("connection", ignoreCase = true) == true
        }
    }
}
