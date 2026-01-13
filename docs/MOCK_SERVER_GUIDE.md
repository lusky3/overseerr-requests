# Mock Overseerr Server - Quick Start Guide

This guide shows you how to use the Mock Overseerr Server for testing the Android app without a real Overseerr instance.

## What is it?

The Mock Overseerr Server is a test utility that simulates a real Overseerr API server. It provides:

- ✅ All Overseerr API endpoints
- ✅ Realistic mock data
- ✅ Pagination support
- ✅ No external dependencies
- ✅ Fast and deterministic responses

## Quick Start

### 1. Add Dependency

The MockWebServer dependency is already added to `app/build.gradle.kts`:

```kotlin
testImplementation(libs.mockwebserver)
```

### 2. Basic Usage

```kotlin
import com.example.overseerr_client.mock.MockOverseerrServer
import com.example.overseerr_client.mock.MockServerTestHelper

// Create and start the server
val mockServer = MockOverseerrServer()
mockServer.start()

// Get the base URL
val baseUrl = mockServer.baseUrl
println("Mock server running at: $baseUrl")

// Create an API service
val authApi = MockServerTestHelper.createApiService<AuthApiService>(mockServer)

// Make API calls
val user = authApi.getCurrentUser()
println("User: ${user.displayName}")

// Cleanup
mockServer.shutdown()
```

### 3. Testing with Kotest

```kotlin
class MyTest : FunSpec({
    lateinit var mockServer: MockOverseerrServer
    
    beforeTest {
        mockServer = MockOverseerrServer()
        mockServer.start()
    }
    
    afterTest {
        mockServer.shutdown()
    }
    
    test("should authenticate user") {
        runTest {
            val api = MockServerTestHelper.createApiService<AuthApiService>(mockServer)
            val response = api.authenticateWithPlex(PlexAuthRequest("token"))
            
            response.apiKey shouldBe "test-api-key-12345"
        }
    }
})
```

## Available Endpoints

### Authentication

```kotlin
val authApi = MockServerTestHelper.createApiService<AuthApiService>(mockServer)

// Authenticate with Plex
val auth = authApi.authenticateWithPlex(PlexAuthRequest("token"))

// Get current user
val user = authApi.getCurrentUser()

// Get server info
val info = authApi.getServerInfo()
```

### Discovery

```kotlin
val discoveryApi = MockServerTestHelper.createApiService<DiscoveryApiService>(mockServer)

// Get trending media
val trending = discoveryApi.getTrending(page = 1)

// Get trending movies
val movies = discoveryApi.getTrendingMovies(page = 1)

// Search for media
val results = discoveryApi.search(query = "inception", page = 1)

// Get movie details
val movie = discoveryApi.getMovieDetails(movieId = 123)

// Get TV show details
val tvShow = discoveryApi.getTvShowDetails(tvId = 456)
```

### Requests

```kotlin
val requestApi = MockServerTestHelper.createApiService<RequestApiService>(mockServer)

// Submit a request
val response = requestApi.submitRequest(
    ApiRequestBody(mediaId = 123, mediaType = "movie")
)

// Get requests list
val requests = requestApi.getRequests(take = 20, skip = 0)

// Get request details
val request = requestApi.getRequest(requestId = 1)

// Get quality profiles
val profiles = requestApi.getQualityProfiles()

// Get root folders
val folders = requestApi.getRootFolders()
```

### User

```kotlin
val userApi = MockServerTestHelper.createApiService<UserApiService>(mockServer)

// Get user profile
val profile = userApi.getUserProfile(userId = 1)

// Get user quota
val quota = userApi.getUserQuota()

// Get user statistics
val stats = userApi.getUserStatistics()
```

## Mock Data Examples

### User Profile

```json
{
  "id": 1,
  "email": "user1@example.com",
  "display_name": "User 1",
  "avatar": "/avatar/1.jpg",
  "request_count": 3,
  "permissions": {
    "can_request": true,
    "can_manage_requests": true,
    "can_view_requests": true,
    "is_admin": true
  }
}
```

### Movie Details

```json
{
  "id": 123,
  "title": "Movie 123",
  "overview": "A detailed overview of movie 123...",
  "poster_path": "/poster/movie_123.jpg",
  "backdrop_path": "/backdrop/movie_123.jpg",
  "release_date": "2024-06-15",
  "vote_average": 8.2,
  "media_info": {
    "status": 1,
    "request_id": null,
    "available": false
  }
}
```

### Search Results

```json
{
  "page": 1,
  "total_pages": 10,
  "total_results": 200,
  "results": [
    {
      "id": 1,
      "media_type": "tv",
      "name": "TV Show 1",
      "overview": "This is a great TV show...",
      "poster_path": "/poster/1.jpg",
      "first_air_date": "2024-01-01",
      "vote_average": 7.0
    }
  ]
}
```

## Testing Scenarios

### Test Successful Authentication

```kotlin
test("should authenticate successfully") {
    runTest {
        val api = createApiService<AuthApiService>()
        val response = api.authenticateWithPlex(PlexAuthRequest("valid-token"))
        
        response.apiKey shouldNotBe null
        response.userId shouldBe 1
    }
}
```

### Test Pagination

```kotlin
test("should handle pagination") {
    runTest {
        val api = createApiService<DiscoveryApiService>()
        
        val page1 = api.getTrending(page = 1)
        val page2 = api.getTrending(page = 2)
        
        page1.page shouldBe 1
        page2.page shouldBe 2
        page1.results[0].id shouldNotBe page2.results[0].id
    }
}
```

### Test Error Handling

```kotlin
test("should handle 404 error") {
    mockServer.enqueue(
        MockResponse()
            .setResponseCode(404)
            .setBody("""{"message": "Not found"}""")
    )
    
    val api = createApiService<DiscoveryApiService>()
    
    assertThrows<HttpException> {
        api.getMovieDetails(movieId = 999999)
    }
}
```

### Test Request Verification

```kotlin
test("should send correct request") {
    runTest {
        val api = createApiService<AuthApiService>()
        api.getServerInfo()
        
        val request = mockServer.takeRequest()
        request.path shouldBe "/api/v1/status"
        request.method shouldBe "GET"
    }
}
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run mock server tests only
./gradlew test --tests "*MockOverseerrServerTest"

# Run with verbose output
./gradlew test --tests "*MockOverseerrServerTest" --info
```

## Using with the App

### Option 1: Test Configuration

Create a test build variant that uses the mock server:

```kotlin
// In your test configuration
@Module
@InstallIn(SingletonComponent::class)
object TestNetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        val mockServer = MockOverseerrServer()
        mockServer.start()
        return mockServer.baseUrl
    }
}
```

### Option 2: Debug Configuration

For manual testing on a device:

1. Start the mock server on your development machine
2. Make it accessible on your network
3. Configure the app to use your machine's IP address

```kotlin
// In debug build
val mockServer = MockOverseerrServer()
mockServer.start(port = 8080)

// In app, use: http://YOUR_IP:8080
```

### Option 3: Instrumented Tests

Use the mock server in instrumented tests:

```kotlin
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    private lateinit var mockServer: MockOverseerrServer
    
    @Before
    fun setup() {
        mockServer = MockOverseerrServer()
        mockServer.start()
        
        // Configure app to use mock server
        // (requires dependency injection setup)
    }
    
    @After
    fun tearDown() {
        mockServer.shutdown()
    }
    
    @Test
    fun testLogin() {
        // Launch activity
        // Perform login
        // Verify success
    }
}
```

## Advanced Features

### Custom Responses

```kotlin
// Override default response
mockServer.enqueue(
    MockResponse()
        .setResponseCode(200)
        .setBody("""{"custom": "data"}""")
)
```

### Network Delays

```kotlin
// Simulate slow network
mockServer.enqueue(
    MockResponse()
        .setResponseCode(200)
        .setBody("""{"data": "value"}""")
        .setBodyDelay(2, TimeUnit.SECONDS)
)
```

### Custom Dispatcher

```kotlin
mockServer.setDispatcher(object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/custom" -> MockResponse().setResponseCode(200)
            else -> MockResponse().setResponseCode(404)
        }
    }
})
```

## Troubleshooting

### Server Won't Start

- Check if port is already in use
- Use `mockServer.start()` without port to use random port

### Serialization Errors

- Verify your models match the mock response structure
- Check `MockResponses.kt` for exact format

### Connection Refused

- Ensure server is started before making requests
- Verify you're using `mockServer.baseUrl`

### Tests Hanging

- Always call `mockServer.shutdown()` in cleanup
- Check for infinite loops in test code

## Best Practices

1. ✅ Always cleanup the server after tests
2. ✅ Use realistic mock data
3. ✅ Test both success and error cases
4. ✅ Verify requests when needed
5. ✅ Isolate tests with fresh server instances
6. ✅ Use meaningful test names
7. ✅ Document custom test scenarios

## Resources

- [Full Documentation](app/src/test/java/com/example/overseerr_client/mock/README.md)
- [Example Tests](app/src/test/java/com/example/overseerr_client/mock/MockOverseerrServerTest.kt)
- [MockWebServer Docs](https://github.com/square/okhttp/tree/master/mockwebserver)
- [Overseerr API Docs](https://api-docs.overseerr.dev/)

## Next Steps

1. Run the example test: `./gradlew test --tests "*MockOverseerrServerTest"`
2. Create your own tests using the mock server
3. Integrate with your repository tests
4. Use for UI testing with instrumented tests

Happy testing! 🚀
