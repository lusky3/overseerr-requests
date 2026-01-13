package app.lusk.client.data.repository

import app.lusk.client.data.remote.api.UserApiService
import app.lusk.client.data.mapper.toDomain
import app.lusk.client.data.remote.safeApiCall
import app.lusk.client.domain.model.Result
import app.lusk.client.domain.model.UserProfile
import app.lusk.client.domain.model.UserStatistics
import app.lusk.client.domain.repository.ProfileRepository
import app.lusk.client.domain.repository.RequestQuota
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
        apiProfile.toDomain()
    }
    
    override suspend fun getUserQuota(): Result<RequestQuota> = safeApiCall {
        val user = userApiService.getCurrentUser()
        val apiQuota = userApiService.getUserQuota(user.id)
        
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
        val user = userApiService.getCurrentUser()
        
        // The statistics endpoint often returns 404 or requires different permissions.
        // For now, we use the basic request count from the user profile.
        UserStatistics(
            totalRequests = user.requestCount,
            approvedRequests = 0,
            declinedRequests = 0,
            pendingRequests = 0,
            availableRequests = 0
        )
    }
}
