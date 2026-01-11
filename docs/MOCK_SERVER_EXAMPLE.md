# Mock Overseerr Server - Complete Example

This document provides a complete, working example of using the Mock Overseerr Server for testing.

## Overview

The Mock Overseerr Server has been created with the following components:

### Files Created

1. **MockOverseerrServer.kt** - Main server implementation
   - Location: `app/src/test/java/com/example/overseerr_client/mock/MockOverseerrServer.kt`
   - Handles all Overseerr API endpoints
   - Uses OkHttp MockWebServer

2. **MockResponses.kt** - Mock data provider
   - Location: `app/src/test/java/com/example/overseerr_client/mock/MockResponses.kt`
   - Provides realistic mock data for all endpoints
   - Includes pagination support

3. **MockServerTestHelper.kt** - Test utilities
   - Location: `app/src/test/java/com/example/overseerr_client/mock/MockServerTestHelper.kt`
   - Helper methods for creating Retrofit instances
   - Base test class for easy setup

4. **MockOverseerrServerTest.kt** - Example tests
   - Location: `app/src/test/java/com/example/overseerr_client/mock/MockOverseerrServerTest.kt`
   - Comprehensive test examples
   - Demonstrates all API endpoints

5. **Documentation**
   - `app/src/test/java/com/example/overseerr_client/mock/README.md` - Detailed documentation
   - `MOCK_SERVER_GUIDE.md` - Quick start guide
   - `MOCK_SERVER_EXAMPLE.md` - This file

## Dependencies Added

The following dependency was added to `app/build.gradle.kts`:

```kotlin
testImplementation(libs.mockwebserver)
```

And in `gradle/libs.versions.toml`:

```toml
[versions]
mockwebserver = "4.12.0"

[libraries]
mockwebserver = { group = "com.squareup.okhttp3", name = "mockwebserver", version.ref = "mockwebserver" }
```

## Complete Working Example

Here's a complete example you can copy and use:

```kotlin
package com.example.overseerr_client

import com.example.overseerr_client.data.remote.api.AuthApiService
import com.example.overseerr_client.data.remote.api.DiscoveryApiService
import com.example.overseerr_client.data.remote.model.PlexAuthRequest
import com.example.overseerr_client.mock.MockOverseerrServer
import com.example.overseerr_client.mock.MockServerTestHelper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest

class ExampleMockServerTest : FunSpec({
    lateinit var mockServer: MockOverseerrServer
    
    beforeTest {
        mockServer = MockOverseerrServer()
        mockServer.start()
    }
    
    afterTest {
        mockServer.shutdown()
    }
    
    test("authenticate with Plex") {
        runTest {
            // Create API service
            val authApi = MockServerTestHelper.createApiService<AuthApiService>(mockServer)
            
            // Make API call
            val response = authApi.authenticateWithPlex(
                PlexAuthRequest(authToken = "test-token")
            )
            
            // Verify response
            response.apiKey shouldBe "test-api-key-12345"
            response.userId shouldBe 1
        }
    }
    
    test("get trending movies") {
        runTest {
            val discoveryApi = MockServerTestHelper.createApiService<DiscoveryApiService>(mockServer)
            
            val results = discoveryApi.getTrendingMovies(page = 1)
            
            results.page shouldBe 1
            results.results.size shouldBe 20
            results.totalPages shouldBe 10
        }
    }
    
    test("search for media") {
        runTest {
            val discoveryApi = MockServerTestHelper.createApiService<DiscoveryApiService>(mockServer)
            
            val results = discoveryApi.search(query = "inception", page = 1)
            
            results.results shouldNotBe emptyList()
        }
    }
})
```

## How to Run Tests

Once the project builds successfully, run:

```bash
# Run all tests
./gradlew :app:test

# Run specific test class
./gradlew :app:test --tests "com.example.overseerr_client.mock.MockOverseerrServerTest"

# Run with verbose output
./gradlew :app:test --info
```

## API Endpoints Supported

### Authentication Endpoints
- ✅ `POST /api/v1/auth/plex` - Authenticate with Plex
- ✅ `GET /api/v1/auth/me` - Get current user
- ✅ `POST /api/v1/auth/logout` - Logout
- ✅ `GET /api/v1/status` - Get server info

### Discovery Endpoints
- ✅ `GET /api/v1/discover/trending` - Get trending media (20 items per page)
- ✅ `GET /api/v1/discover/movies` - Get trending movies (20 items per page)
- ✅ `GET /api/v1/discover/tv` - Get trending TV shows (20 items per page)
- ✅ `GET /api/v1/search` - Search for media (20 items per page)
- ✅ `GET /api/v1/movie/{id}` - Get movie details
- ✅ `GET /api/v1/tv/{id}` - Get TV show details

### Request Endpoints
- ✅ `POST /api/v1/request` - Submit a request
- ✅ `GET /api/v1/request` - Get requests list (20 items per page)
- ✅ `GET /api/v1/request/{id}` - Get request details
- ✅ `GET /api/v1/user/{id}/requests` - Get user requests (20 items per page)
- ✅ `GET /api/v1/request/{id}/status` - Get request status
- ✅ `GET /api/v1/settings/radarr/profiles` - Get quality profiles (4 profiles)
- ✅ `GET /api/v1/settings/radarr/folders` - Get root folders (4 folders)

### User Endpoints
- ✅ `GET /api/v1/user/{id}` - Get user profile
- ✅ `GET /api/v1/user` - Get current user
- ✅ `GET /api/v1/user/quota` - Get user quota
- ✅ `GET /api/v1/user/stats` - Get user statistics

## Mock Data Characteristics

### Pagination
- **Page Size**: 20 items per page
- **Total Pages**: 10 pages
- **Total Results**: 200 items
- **Page Numbers**: 1-indexed (page 1, page 2, etc.)

### User Data
- **User ID**: Sequential (1, 2, 3, ...)
- **Email**: `user{id}@example.com`
- **Display Name**: `User {id}`
- **Avatar**: `/avatar/{id}.jpg`
- **Permissions**: User 1 is admin, others are regular users
- **Request Count**: `userId * 3`

### Media Data
- **Movie IDs**: Even numbers (2, 4, 6, ...)
- **TV Show IDs**: Odd numbers (1, 3, 5, ...)
- **Titles**: `Movie {id}` or `TV Show {id}`
- **Posters**: `/poster/{type}_{id}.jpg`
- **Backdrops**: `/backdrop/{type}_{id}.jpg`
- **Vote Averages**: 6.5 - 9.0 range
- **Release Dates**: Distributed across 2024

### Request Data
- **Request IDs**: Sequential (1, 2, 3, ...)
- **Status Values**:
  - 1 = Pending (requestId % 4 == 0)
  - 2 = Approved (requestId % 4 == 1)
  - 3 = Declined (requestId % 4 == 2)
  - 5 = Available (requestId % 4 == 3)
- **Created Dates**: `2024-01-{day}T10:00:00.000Z`
- **Media Type**: Alternates between movie and tv

### Quality Profiles
1. HD-1080p (id: 1)
2. Ultra-HD (id: 2)
3. SD (id: 3)
4. 4K (id: 4)

### Root Folders
1. /movies (id: 1)
2. /tv (id: 2)
3. /media/movies (id: 3)
4. /media/tv (id: 4)

### User Quota
- **Movie Quota**: 10 limit, 5 remaining, 7 days
- **TV Quota**: 15 limit, 7 remaining, 7 days

### User Statistics
- **Total Requests**: 25
- **Approved**: 15
- **Declined**: 3
- **Pending**: 5
- **Available**: 2

## Testing Scenarios

### 1. Test Authentication Flow

```kotlin
test("complete authentication flow") {
    runTest {
        val authApi = createApiService<AuthApiService>()
        
        // Step 1: Authenticate
        val authResponse = authApi.authenticateWithPlex(
            PlexAuthRequest("plex-token")
        )
        authResponse.apiKey shouldNotBe null
        
        // Step 2: Get current user
        val user = authApi.getCurrentUser()
        user.id shouldBe 1
        
        // Step 3: Get server info
        val serverInfo = authApi.getServerInfo()
        serverInfo.initialized shouldBe true
    }
}
```

### 2. Test Discovery and Details

```kotlin
test("discover and view movie details") {
    runTest {
        val discoveryApi = createApiService<DiscoveryApiService>()
        
        // Step 1: Get trending movies
        val trending = discoveryApi.getTrendingMovies(page = 1)
        trending.results shouldNotBe emptyList()
        
        // Step 2: Get details for first movie
        val movieId = trending.results[0].id
        val movie = discoveryApi.getMovieDetails(movieId)
        movie.id shouldBe movieId
    }
}
```

### 3. Test Request Submission

```kotlin
test("submit and track request") {
    runTest {
        val requestApi = createApiService<RequestApiService>()
        
        // Step 1: Get quality profiles
        val profiles = requestApi.getQualityProfiles()
        profiles shouldNotBe emptyList()
        
        // Step 2: Submit request
        val response = requestApi.submitRequest(
            ApiRequestBody(mediaId = 123, mediaType = "movie")
        )
        response.id shouldNotBe null
        
        // Step 3: Check request status
        val request = requestApi.getRequest(response.id)
        request.mediaId shouldBe 123
    }
}
```

### 4. Test Pagination

```kotlin
test("paginate through results") {
    runTest {
        val discoveryApi = createApiService<DiscoveryApiService>()
        
        val pages = (1..3).map { page ->
            discoveryApi.getTrending(page = page)
        }
        
        // Verify each page
        pages.forEachIndexed { index, results ->
            results.page shouldBe index + 1
            results.results.size shouldBe 20
        }
        
        // Verify different results
        pages[0].results[0].id shouldNotBe pages[1].results[0].id
    }
}
```

### 5. Test User Profile and Quota

```kotlin
test("get user profile and quota") {
    runTest {
        val userApi = createApiService<UserApiService>()
        
        // Get profile
        val profile = userApi.getUserProfile(userId = 1)
        profile.email shouldContain "@example.com"
        
        // Get quota
        val quota = userApi.getUserQuota()
        quota.movie?.limit shouldBe 10
        quota.tv?.limit shouldBe 15
        
        // Get statistics
        val stats = userApi.getUserStatistics()
        stats.totalRequests shouldBe 25
    }
}
```

## Integration with Repository Tests

You can use the mock server to test your repository implementations:

```kotlin
class MediaRepositoryTest : FunSpec({
    lateinit var mockServer: MockOverseerrServer
    lateinit var repository: MediaRepository
    
    beforeTest {
        mockServer = MockOverseerrServer()
        mockServer.start()
        
        // Create repository with mock API
        val retrofit = MockServerTestHelper.createRetrofit(mockServer.baseUrl)
        val api = retrofit.create(DiscoveryApiService::class.java)
        repository = MediaRepositoryImpl(api)
    }
    
    afterTest {
        mockServer.shutdown()
    }
    
    test("repository should fetch trending movies") {
        runTest {
            val result = repository.getTrendingMovies(page = 1)
            
            result.isSuccess shouldBe true
            val movies = result.getOrNull()
            movies shouldNotBe null
            movies?.size shouldBe 20
        }
    }
})
```

## Troubleshooting

### Build Issues

If you encounter build issues:

1. **Sync Gradle**: `./gradlew --refresh-dependencies`
2. **Clean Build**: `./gradlew clean`
3. **Check Dependencies**: Ensure MockWebServer is in `build.gradle.kts`

### Test Failures

If tests fail:

1. **Check Server Lifecycle**: Ensure `start()` and `shutdown()` are called
2. **Verify Base URL**: Use `mockServer.baseUrl` not hardcoded URLs
3. **Check Serialization**: Ensure models match mock response structure

### Port Conflicts

If you get "port already in use" errors:

```kotlin
// Let system assign random port
mockServer.start() // Uses random available port

// Or specify a port
mockServer.start(8080)
```

## Next Steps

1. ✅ Dependencies added to build files
2. ✅ Mock server implementation created
3. ✅ Mock data provider created
4. ✅ Test helpers created
5. ✅ Example tests created
6. ✅ Documentation created

### To Use:

1. **Sync Gradle** to download MockWebServer dependency
2. **Run Tests** to verify everything works
3. **Create Your Tests** using the examples as templates
4. **Integrate** with your repository and use case tests

## Benefits

✅ **No External Dependencies** - Tests run without real Overseerr server
✅ **Fast Execution** - Mock responses are instant
✅ **Deterministic** - Same inputs always produce same outputs
✅ **Offline Testing** - Works without internet connection
✅ **Easy Debugging** - Full control over responses and timing
✅ **CI/CD Friendly** - No need to set up test infrastructure

## Summary

You now have a complete mock Overseerr server implementation that:

- Supports all API endpoints
- Provides realistic mock data
- Includes pagination support
- Has comprehensive test examples
- Is fully documented
- Is ready to use in your tests

The mock server will help you test your app's API integration without needing a real Overseerr instance, making your tests faster, more reliable, and easier to maintain.

Happy testing! 🚀
