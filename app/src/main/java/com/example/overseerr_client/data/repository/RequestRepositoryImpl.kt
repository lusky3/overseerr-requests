package com.example.overseerr_client.data.repository

import com.example.overseerr_client.data.local.dao.MediaRequestDao
import com.example.overseerr_client.data.mapper.toDomain
import com.example.overseerr_client.data.mapper.toEntity
import com.example.overseerr_client.data.remote.model.toMediaRequest
import com.example.overseerr_client.data.remote.api.RequestApiService
import com.example.overseerr_client.data.remote.safeApiCall
import com.example.overseerr_client.domain.model.MediaRequest
import com.example.overseerr_client.domain.model.RequestStatus
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.repository.QualityProfile
import com.example.overseerr_client.domain.repository.RequestRepository
import com.example.overseerr_client.domain.repository.RootFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of RequestRepository.
 * Feature: overseerr-android-client
 * Validates: Requirements 3.1, 3.2, 4.1, 4.4
 */
@Singleton
class RequestRepositoryImpl @Inject constructor(
    private val requestApiService: RequestApiService,
    private val mediaRequestDao: MediaRequestDao
) : RequestRepository {
    
    /**
     * Request a movie.
     * Property 9: Request Submission Completeness
     */
    override suspend fun requestMovie(
        movieId: Int,
        qualityProfile: Int?,
        rootFolder: String?
    ): Result<MediaRequest> = safeApiCall {
        val response = requestApiService.requestMovie(
            movieId = movieId,
            qualityProfile = qualityProfile,
            rootFolder = rootFolder
        )
        
        val mediaRequest = response.toMediaRequest()
        
        // Cache the request locally
        mediaRequestDao.insert(mediaRequest.toEntity())
        
        mediaRequest
    }
    
    /**
     * Request a TV show with specific seasons.
     * Property 9: Request Submission Completeness
     */
    override suspend fun requestTvShow(
        tvShowId: Int,
        seasons: List<Int>,
        qualityProfile: Int?,
        rootFolder: String?
    ): Result<MediaRequest> = safeApiCall {
        val response = requestApiService.requestTvShow(
            tvShowId = tvShowId,
            seasons = seasons,
            qualityProfile = qualityProfile,
            rootFolder = rootFolder
        )
        
        val mediaRequest = response.toMediaRequest()
        
        // Cache the request locally
        mediaRequestDao.insert(mediaRequest.toEntity())
        
        mediaRequest
    }
    
    /**
     * Get all user requests.
     * Property 13: Request List Completeness
     */
    override fun getUserRequests(): Flow<List<MediaRequest>> {
        return mediaRequestDao.getAllRequests()
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    /**
     * Get requests filtered by status.
     * Property 14: Request Grouping Correctness
     */
    override fun getRequestsByStatus(status: RequestStatus): Flow<List<MediaRequest>> {
        return mediaRequestDao.getRequestsByStatus(status.name)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    /**
     * Cancel a pending request.
     * Property 16: Permission-Based Cancellation
     */
    override suspend fun cancelRequest(requestId: Int): Result<Unit> = safeApiCall {
        requestApiService.cancelRequest(requestId)
        
        // Remove from local cache
        mediaRequestDao.deleteById(requestId)
    }
    
    /**
     * Get request status.
     * Property 17: Request Status Updates
     */
    override suspend fun getRequestStatus(requestId: Int): Result<RequestStatus> = safeApiCall {
        val response = requestApiService.getRequestStatus(requestId)
        RequestStatus.valueOf(response.status.uppercase())
    }
    
    /**
     * Refresh requests from server.
     * Property 18: Pull-to-Refresh Data Freshness
     */
    override suspend fun refreshRequests(): Result<Unit> = safeApiCall {
        // Get current user's requests (would need user ID from auth)
        val response = requestApiService.getRequests(take = 100, skip = 0)
        val requests = response.results.map { it.toMediaRequest() }
        
        // Clear old cache and insert fresh data
        mediaRequestDao.deleteAll()
        requests.forEach { request ->
            mediaRequestDao.insert(request.toEntity())
        }
    }
    
    /**
     * Check if media is already requested.
     * Property 11: Duplicate Request Prevention
     */
    override suspend fun isMediaRequested(mediaId: Int): Result<Boolean> = safeApiCall {
        val request = mediaRequestDao.getRequestByMediaId(mediaId)
        request != null
    }
    
    /**
     * Get available quality profiles.
     * Property 12: Advanced Options Availability
     */
    override suspend fun getQualityProfiles(): Result<List<QualityProfile>> = safeApiCall {
        val response = requestApiService.getQualityProfiles()
        response.map { QualityProfile(id = it.id, name = it.name) }
    }
    
    /**
     * Get available root folders.
     * Property 12: Advanced Options Availability
     */
    override suspend fun getRootFolders(): Result<List<RootFolder>> = safeApiCall {
        val response = requestApiService.getRootFolders()
        response.map { RootFolder(id = it.id, path = it.path) }
    }
}
