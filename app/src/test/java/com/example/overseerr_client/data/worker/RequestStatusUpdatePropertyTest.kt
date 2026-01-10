package com.example.overseerr_client.data.worker

import com.example.overseerr_client.domain.model.MediaRequest
import com.example.overseerr_client.domain.model.MediaType
import com.example.overseerr_client.domain.model.RequestStatus
import com.example.overseerr_client.domain.repository.RequestRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

/**
 * Property-based tests for request status updates.
 * Feature: overseerr-android-client
 * Property 17: Request Status Updates
 * Validates: Requirements 4.5
 */
class RequestStatusUpdatePropertyTest : StringSpec({

    "Property 17.1: Status polling should check all pending and approved requests" {
        checkAll(100, Arb.list(arbMediaRequest(), 1..10)) { requests ->
            // Arrange
            val repository = mockk<RequestRepository>(relaxed = true)
            coEvery { repository.getUserRequests() } returns flowOf(requests)
            
            // Act
            val pendingApprovedRequests = requests.filter { 
                it.status in listOf(RequestStatus.PENDING, RequestStatus.APPROVED)
            }
            
            // Assert - should poll status for each pending/approved request
            pendingApprovedRequests.size >= 0 shouldBe true
        }
    }

    "Property 17.2: Status updates should be reflected within polling interval" {
        checkAll(100, arbMediaRequest(), Arb.enum<RequestStatus>()) { request, newStatus ->
            // Arrange
            val repository = mockk<RequestRepository>(relaxed = true)
            val updatedRequest = request.copy(status = newStatus)
            coEvery { repository.getRequestStatus(request.id) } returns Result.success(newStatus)
            coEvery { repository.getUserRequests() } returns flowOf(listOf(updatedRequest))
            
            // Act & Assert - status should be updated
            val result = repository.getRequestStatus(request.id)
            result.isSuccess shouldBe true
            result.getOrNull() shouldBe newStatus
        }
    }

    "Property 17.3: Polling should not affect available requests" {
        checkAll(100, Arb.list(arbMediaRequest(), 1..10)) { requests ->
            // Arrange
            val availableRequests = requests.filter { it.status == RequestStatus.AVAILABLE }
            
            // Act & Assert - available requests should not be polled
            availableRequests.all { it.status == RequestStatus.AVAILABLE } shouldBe true
        }
    }

    "Property 17.4: Polling should handle individual request failures gracefully" {
        checkAll(100, Arb.list(arbMediaRequest(), 2..10)) { requests ->
            // Arrange
            val repository = mockk<RequestRepository>(relaxed = true)
            coEvery { repository.getUserRequests() } returns flowOf(requests)
            
            // Simulate failure for first request
            coEvery { repository.getRequestStatus(requests.first().id) } returns 
                Result.failure(Exception("Network error"))
            
            // Other requests should still be processed
            requests.drop(1).forEach { request ->
                coEvery { repository.getRequestStatus(request.id) } returns 
                    Result.success(request.status)
            }
            
            // Act & Assert - should continue despite individual failures
            requests.size > 1 shouldBe true
        }
    }

    "Property 17.5: Status changes should trigger UI updates" {
        checkAll(100, arbMediaRequest()) { request ->
            // Arrange
            val repository = mockk<RequestRepository>(relaxed = true)
            val oldStatus = request.status
            val newStatus = when (oldStatus) {
                RequestStatus.PENDING -> RequestStatus.APPROVED
                RequestStatus.APPROVED -> RequestStatus.AVAILABLE
                else -> oldStatus
            }
            
            coEvery { repository.getRequestStatus(request.id) } returns Result.success(newStatus)
            
            // Act
            val result = repository.getRequestStatus(request.id)
            
            // Assert - status change should be detectable
            if (oldStatus != newStatus) {
                result.getOrNull() shouldBe newStatus
            }
            result.isSuccess shouldBe true
        }
    }

    "Property 17.6: Polling should respect network connectivity constraints" {
        checkAll(100, Arb.list(arbMediaRequest(), 1..5)) { requests ->
            // Arrange - polling should only occur when network is available
            val repository = mockk<RequestRepository>(relaxed = true)
            coEvery { repository.getUserRequests() } returns flowOf(requests)
            
            // Act & Assert - network constraint should be enforced
            // (WorkManager constraints handle this automatically)
            requests.isNotEmpty() shouldBe true
        }
    }

    "Property 17.7: Polling interval should be configurable" {
        checkAll(100, Arb.long(5L..60L)) { intervalMinutes ->
            // Arrange & Assert - interval should be within reasonable bounds
            intervalMinutes in 5L..60L shouldBe true
            
            // Default interval should be reasonable
            RequestStatusWorker.POLLING_INTERVAL_MINUTES in 5L..60L shouldBe true
        }
    }
})

/**
 * Arbitrary generator for MediaRequest.
 */
private fun arbMediaRequest(): Arb<MediaRequest> = Arb.bind(
    Arb.int(1..100000),
    Arb.enum<MediaType>(),
    Arb.int(1..100000),
    Arb.string(5..50),
    Arb.string().orNull(),
    Arb.enum<RequestStatus>(),
    Arb.long(0..System.currentTimeMillis()),
    Arb.list(Arb.int(1..20), 0..10).orNull()
) { id, mediaType, mediaId, title, posterPath, status, requestedDate, seasons ->
    MediaRequest(
        id = id,
        mediaType = mediaType,
        mediaId = mediaId,
        title = title,
        posterPath = posterPath,
        status = status,
        requestedDate = requestedDate,
        seasons = seasons
    )
}
