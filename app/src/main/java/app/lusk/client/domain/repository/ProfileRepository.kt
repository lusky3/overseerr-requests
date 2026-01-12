package app.lusk.client.domain.repository

import app.lusk.client.domain.model.Result
import app.lusk.client.domain.model.UserProfile
import app.lusk.client.domain.model.UserStatistics

/**
 * Repository interface for user profile operations.
 * Feature: overseerr-android-client
 * Validates: Requirements 5.1
 */
interface ProfileRepository {
    
    /**
     * Get user profile information.
     * Property 19: Profile Information Completeness
     */
    suspend fun getUserProfile(): Result<UserProfile>
    
    /**
     * Get user request quota.
     * Property 19: Profile Information Completeness
     */
    suspend fun getUserQuota(): Result<RequestQuota>
    
    /**
     * Get user statistics.
     * Property 19: Profile Information Completeness
     */
    suspend fun getUserStatistics(): Result<UserStatistics>
}

/**
 * User request quota information.
 */
data class RequestQuota(
    val movieLimit: Int?,
    val movieRemaining: Int?,
    val movieDays: Int?,
    val tvLimit: Int?,
    val tvRemaining: Int?,
    val tvDays: Int?
)
