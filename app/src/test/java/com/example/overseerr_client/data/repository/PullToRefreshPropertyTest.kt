package com.example.overseerr_client.data.repository

import com.example.overseerr_client.data.local.dao.MediaRequestDao
import com.example.overseerr_client.data.local.entity.MediaRequestEntity
import com.example.overseerr_client.data.remote.SafeApiCall
import com.example.overseerr_client.data.remote.api.RequestApiService
import com.example.overseerr_client.data.remote.model.ApiMediaRequest
import com.example.overseerr_client.data.remote.model.ApiRequestsResponse
import com.example.overseerr_client.domain.model.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

/**
 * Property-based tests for pull-to-refresh data freshness.
 * Feature: overseerr-android-client, Property 18: Pull-to-Refresh Data Freshness
 * Validates: Requirements 4.6
 */
class PullToRefreshPropertyTest : StringSpec({
    
    "Property 18.1: Pull-to-refresh fetches latest data from server" {
        checkAll<List<Triple<Int, String, Long>>>(100, Arb.list(
            Arb.int(1..1000) to Arb.string(1..100) to Arb.long(0..System.currentTimeMillis()),
            1..20
        )) { requests ->
            // Given - old cached data
            val apiService = mockk<RequestApiService>()
            val dao = mockk<MediaRequestDao>()
            val safeApiCall = SafeApiCall()
            
            val oldEntities = requests.map { (id, title, timestamp) ->
                MediaRequestEntity(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = title,
                    posterPath = null,
                    status = "pending",
                    requestedDate = timestamp,
                    seasons = null
                )
            }
            
            // New data from server with updated timestamps
            val newApiRequests = requests.map { (id, title, _) ->
                ApiMediaRequest(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = "$title (Updated)",
                    posterPath = null,
                    status = "approved",
                    requestedDate = System.currentTimeMillis(),
                    seasons = null
                )
            }
            
            coEvery { dao.getAllRequests() } returns flowOf(oldEntities)
            coEvery { apiService.getUserRequests(any(), any()) } returns ApiRequestsResponse(
                results = newApiRequests,
                page = 1,
                totalPages = 1,
                totalResults = newApiRequests.size
            )
            coEvery { dao.insertRequests(any()) } returns Unit
            
            val repository = RequestRepositoryImpl(apiService, dao, safeApiCall)
            
            // When - refresh is triggered
            val refreshResult = repository.refreshRequests()
            
            // Then - API should be called
            refreshResult.shouldBeInstanceOf<Result.Success<Unit>>()
            coVerify { apiService.getUserRequests(any(), any()) }
            coVerify { dao.insertRequests(any()) }
        }
    }
    
    "Property 18.2: Refreshed data replaces cached data" {
        checkAll<List<Pair<Int, String>>>(100, Arb.list(
            Arb.int(1..1000) to Arb.string(1..100),
            1..10
        )) { requests ->
            // Given
            val apiService = mockk<RequestApiService>()
            val dao = mockk<MediaRequestDao>()
            val safeApiCall = SafeApiCall()
            
            val oldEntities = requests.map { (id, title) ->
                MediaRequestEntity(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = title,
                    posterPath = null,
                    status = "pending",
                    requestedDate = System.currentTimeMillis() - 86400000, // 1 day ago
                    seasons = null
                )
            }
            
            val newApiRequests = requests.map { (id, title) ->
                ApiMediaRequest(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = "$title (Fresh)",
                    posterPath = null,
                    status = "available",
                    requestedDate = System.currentTimeMillis(),
                    seasons = null
                )
            }
            
            val newEntities = newApiRequests.map { apiRequest ->
                MediaRequestEntity(
                    id = apiRequest.id,
                    mediaType = apiRequest.mediaType,
                    mediaId = apiRequest.mediaId,
                    title = apiRequest.title,
                    posterPath = apiRequest.posterPath,
                    status = apiRequest.status,
                    requestedDate = apiRequest.requestedDate,
                    seasons = apiRequest.seasons
                )
            }
            
            coEvery { dao.getAllRequests() } returnsMany listOf(
                flowOf(oldEntities),
                flowOf(newEntities)
            )
            coEvery { apiService.getUserRequests(any(), any()) } returns ApiRequestsResponse(
                results = newApiRequests,
                page = 1,
                totalPages = 1,
                totalResults = newApiRequests.size
            )
            coEvery { dao.insertRequests(any()) } returns Unit
            
            val repository = RequestRepositoryImpl(apiService, dao, safeApiCall)
            
            // When
            val beforeRefresh = repository.getUserRequests().first()
            repository.refreshRequests()
            val afterRefresh = repository.getUserRequests().first()
            
            // Then - data should be updated
            beforeRefresh.all { it.status.name == "PENDING" } shouldBe true
            afterRefresh.all { it.status.name == "AVAILABLE" } shouldBe true
        }
    }
    
    "Property 18.3: Refresh handles network errors gracefully" {
        checkAll<Int>(100) { seed ->
            // Given - network error
            val apiService = mockk<RequestApiService>()
            val dao = mockk<MediaRequestDao>()
            val safeApiCall = SafeApiCall()
            
            coEvery { apiService.getUserRequests(any(), any()) } throws 
                java.net.UnknownHostException("Network unavailable")
            
            val repository = RequestRepositoryImpl(apiService, dao, safeApiCall)
            
            // When
            val result = repository.refreshRequests()
            
            // Then - should return error but not crash
            result.shouldBeInstanceOf<Result.Error>()
            
            // Database should not be modified
            coVerify(exactly = 0) { dao.insertRequests(any()) }
        }
    }
    
    "Property 18.4: Refresh preserves request IDs" {
        checkAll<List<Int>>(100, Arb.list(Arb.int(1..10000), 1..15)) { requestIds ->
            // Given
            val apiService = mockk<RequestApiService>()
            val dao = mockk<MediaRequestDao>()
            val safeApiCall = SafeApiCall()
            
            val apiRequests = requestIds.map { id ->
                ApiMediaRequest(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = "Movie $id",
                    posterPath = null,
                    status = "pending",
                    requestedDate = System.currentTimeMillis(),
                    seasons = null
                )
            }
            
            val entities = apiRequests.map { apiRequest ->
                MediaRequestEntity(
                    id = apiRequest.id,
                    mediaType = apiRequest.mediaType,
                    mediaId = apiRequest.mediaId,
                    title = apiRequest.title,
                    posterPath = apiRequest.posterPath,
                    status = apiRequest.status,
                    requestedDate = apiRequest.requestedDate,
                    seasons = apiRequest.seasons
                )
            }
            
            coEvery { dao.getAllRequests() } returns flowOf(entities)
            coEvery { apiService.getUserRequests(any(), any()) } returns ApiRequestsResponse(
                results = apiRequests,
                page = 1,
                totalPages = 1,
                totalResults = apiRequests.size
            )
            coEvery { dao.insertRequests(any()) } returns Unit
            
            val repository = RequestRepositoryImpl(apiService, dao, safeApiCall)
            
            // When
            repository.refreshRequests()
            val result = repository.getUserRequests().first()
            
            // Then - all original IDs should be present
            val resultIds = result.map { it.id }
            resultIds shouldContainAll requestIds
        }
    }
    
    "Property 18.5: Refresh updates status changes" {
        checkAll<List<Pair<Int, String>>>(100, Arb.list(
            Arb.int(1..1000) to Arb.string(1..100),
            1..10
        )) { requests ->
            // Given - requests with old status
            val apiService = mockk<RequestApiService>()
            val dao = mockk<MediaRequestDao>()
            val safeApiCall = SafeApiCall()
            
            val oldStatuses = listOf("pending", "approved")
            val newStatuses = listOf("available", "declined")
            
            val oldEntities = requests.mapIndexed { index, (id, title) ->
                MediaRequestEntity(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = title,
                    posterPath = null,
                    status = oldStatuses[index % oldStatuses.size],
                    requestedDate = System.currentTimeMillis(),
                    seasons = null
                )
            }
            
            val newApiRequests = requests.mapIndexed { index, (id, title) ->
                ApiMediaRequest(
                    id = id,
                    mediaType = "movie",
                    mediaId = id,
                    title = title,
                    posterPath = null,
                    status = newStatuses[index % newStatuses.size],
                    requestedDate = System.currentTimeMillis(),
                    seasons = null
                )
            }
            
            val newEntities = newApiRequests.map { apiRequest ->
                MediaRequestEntity(
                    id = apiRequest.id,
                    mediaType = apiRequest.mediaType,
                    mediaId = apiRequest.mediaId,
                    title = apiRequest.title,
                    posterPath = apiRequest.posterPath,
                    status = apiRequest.status,
                    requestedDate = apiRequest.requestedDate,
                    seasons = apiRequest.seasons
                )
            }
            
            coEvery { dao.getAllRequests() } returnsMany listOf(
                flowOf(oldEntities),
                flowOf(newEntities)
            )
            coEvery { apiService.getUserRequests(any(), any()) } returns ApiRequestsResponse(
                results = newApiRequests,
                page = 1,
                totalPages = 1,
                totalResults = newApiRequests.size
            )
            coEvery { dao.insertRequests(any()) } returns Unit
            
            val repository = RequestRepositoryImpl(apiService, dao, safeApiCall)
            
            // When
            val beforeRefresh = repository.getUserRequests().first()
            repository.refreshRequests()
            val afterRefresh = repository.getUserRequests().first()
            
            // Then - statuses should be updated
            val beforeStatuses = beforeRefresh.map { it.status.name.lowercase() }.toSet()
            val afterStatuses = afterRefresh.map { it.status.name.lowercase() }.toSet()
            
            beforeStatuses shouldContainAll oldStatuses
            afterStatuses shouldContainAll newStatuses
        }
    }
})
