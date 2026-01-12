package app.lusk.client.data.repository

import app.lusk.client.data.local.dao.MediaRequestDao
import app.lusk.client.data.mapper.toDomain
import app.lusk.client.data.mapper.toEntity
import app.lusk.client.data.remote.model.toMediaRequest
import app.lusk.client.data.remote.api.RequestApiService
import app.lusk.client.data.remote.safeApiCall
import app.lusk.client.domain.model.MediaRequest
import app.lusk.client.domain.model.RequestStatus
import app.lusk.client.domain.model.Result
import app.lusk.client.domain.repository.QualityProfile
import app.lusk.client.domain.repository.RequestRepository
import app.lusk.client.domain.repository.RootFolder
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
    private val mediaRequestDao: MediaRequestDao,
    private val offlineRequestDao: app.lusk.client.data.local.dao.OfflineRequestDao,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) : RequestRepository {
    
    private fun scheduleSync() {
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<app.lusk.client.worker.OfflineRequestWorker>()
            .setConstraints(
                androidx.work.Constraints.Builder()
                    .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                    .build()
            )
            .build()
            
        androidx.work.WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_requests",
            androidx.work.ExistingWorkPolicy.APPEND,
            workRequest
        )
    }
    
    /**
     * Request a movie.
     * Property 9: Request Submission Completeness
     */
    override suspend fun requestMovie(
        movieId: Int,
        qualityProfile: Int?,
        rootFolder: String?
    ): Result<MediaRequest> {
        val result = safeApiCall {
            requestApiService.requestMovie(
                movieId = movieId,
                qualityProfile = qualityProfile,
                rootFolder = rootFolder
            )
        }

        return when (result) {
            is Result.Success -> {
                val mediaRequest = result.data.toMediaRequest()
                mediaRequestDao.insert(mediaRequest.toEntity())
                Result.success(mediaRequest)
            }
            is Result.Error -> {
                val error = result.error
                if (error is app.lusk.client.domain.model.AppError.NetworkError || 
                    error is app.lusk.client.domain.model.AppError.TimeoutError) {
                    
                    // Queue for offline
                    val offlineRequest = app.lusk.client.data.local.entity.OfflineRequestEntity(
                        mediaType = "movie",
                        mediaId = movieId,
                        seasons = null,
                        qualityProfile = qualityProfile,
                        rootFolder = rootFolder
                    )
                    offlineRequestDao.insert(offlineRequest)
                    
                    // Also save to MediaRequestDao for UI immediate feedback
                    val dummyRequest = MediaRequest(
                        id = -movieId, // Negative ID to indicate local/temporary
                        mediaType = app.lusk.client.domain.model.MediaType.MOVIE,
                        mediaId = movieId,
                        title = "Queued Request",
                        posterPath = null,
                        status = RequestStatus.PENDING,
                        requestedDate = System.currentTimeMillis(),
                        seasons = null,
                        isOfflineQueued = true
                    )
                    mediaRequestDao.insert(dummyRequest.toEntity())
                    
                    scheduleSync()
                    
                    Result.success(dummyRequest)
                } else {
                    Result.error(error)
                }
            }
            else -> result.map { it.toMediaRequest() } // Loading
        }
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
    ): Result<MediaRequest> {
        val result = safeApiCall {
            requestApiService.requestTvShow(
                tvShowId = tvShowId,
                seasons = seasons,
                qualityProfile = qualityProfile,
                rootFolder = rootFolder
            )
        }

        return when (result) {
            is Result.Success -> {
                val mediaRequest = result.data.toMediaRequest()
                mediaRequestDao.insert(mediaRequest.toEntity())
                Result.success(mediaRequest)
            }
            is Result.Error -> {
                val error = result.error
                if (error is app.lusk.client.domain.model.AppError.NetworkError || 
                    error is app.lusk.client.domain.model.AppError.TimeoutError) {
                    
                    // Queue for offline
                    val offlineRequest = app.lusk.client.data.local.entity.OfflineRequestEntity(
                        mediaType = "tv",
                        mediaId = tvShowId,
                        seasons = seasons.joinToString(","),
                        qualityProfile = qualityProfile,
                        rootFolder = rootFolder
                    )
                    offlineRequestDao.insert(offlineRequest)
                    
                    val dummyRequest = MediaRequest(
                        id = -tvShowId,
                        mediaType = app.lusk.client.domain.model.MediaType.TV,
                        mediaId = tvShowId,
                        title = "Queued TV Request",
                        posterPath = null,
                        status = RequestStatus.PENDING,
                        requestedDate = System.currentTimeMillis(),
                        seasons = seasons,
                        isOfflineQueued = true
                    )
                    mediaRequestDao.insert(dummyRequest.toEntity())
                    
                    scheduleSync()
                    
                    Result.success(dummyRequest)
                } else {
                    Result.error(error)
                }
            }
            else -> result.map { it.toMediaRequest() }
        }
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
