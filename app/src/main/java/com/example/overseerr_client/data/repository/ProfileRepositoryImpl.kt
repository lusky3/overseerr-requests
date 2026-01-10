package com.example.overseerr_client.data.repository

import com.example.overseerr_client.data.remote.api.UserApiService
import com.example.overseerr_client.data.remote.model.toPermissions
import com.example.overseerr_client.data.remote.safeApiCall
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.model.UserProfile
import com.example.overseerr_client.domain.model.UserStatistics
import com.example.overseerr_client.domain.repository.ProfileRepository
import com.example.overseerr_client.domain.repository.RequestQuota
import javax.inject.Inject

/**
 * Implementation of ProfileRepository.
 * Feature: overseerr-android-client
 * Validates: Requirements 5.1
 */
class ProfileRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : ProfileRepository {
    
    override suspend fun getUserProfile(): Result<UserProfile> = safeApiCall {
        val apiProfile = userApiService.getCurrentUser()
        
        UserProfile(
            id = apiProfile.id,
            email = apiProfile.email,
            displayName = apiProfile.displayName ?: apiProfile.email,
            avatar = apiProfile.avatar,
            requestCount = apiProfile.requestCount ?: 0,
            permissions = apiProfile.permissions.toPermissions()
        )
    }
    
    override suspend fun getUserQuota(): Result<RequestQuota> = safeApiCall {
        val apiQuota = userApiService.getUserQuota()
        
        RequestQuota(
            movieLimit = apiQuota.movie?.limit,
            movieRemaining = apiQuota.movie?.remaining,
            movieDays = apiQuota.movie?.days,
            tvLimit = apiQuota.tv?.limit,
            tvRemaining = apiQuota.tv?.remaining,
            tvDays = apiQuota.tv?.days
        )
    }
    
    override suspend fun getUserStatistics(): Result<UserStatistics> = safeApiCall {
        val apiStats = userApiService.getUserStatistics()
        
        UserStatistics(
            totalRequests = apiStats.totalRequests,
            approvedRequests = apiStats.approvedRequests,
            declinedRequests = apiStats.declinedRequests,
            pendingRequests = apiStats.pendingRequests,
            availableRequests = apiStats.availableRequests
        )
    }
}
