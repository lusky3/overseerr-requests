package app.lusk.client.mock

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit

/**
 * Mock Overseerr server for testing using MockWebServer.
 * Provides realistic API responses for all Overseerr endpoints.
 */
class MockOverseerrServer {
    private val server = MockWebServer()
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    val baseUrl: String
        get() = server.url("/").toString()
    
    val port: Int
        get() = server.port
    
    /**
     * Start the mock server with default responses.
     */
    fun start() {
        server.dispatcher = OverseerrDispatcher()
        server.start()
    }
    
    /**
     * Start the mock server on a specific port.
     */
    fun start(port: Int) {
        server.dispatcher = OverseerrDispatcher()
        server.start(port)
    }
    
    /**
     * Shutdown the mock server.
     */
    fun shutdown() {
        server.shutdown()
    }
    
    /**
     * Enqueue a custom response.
     */
    fun enqueue(response: MockResponse) {
        server.enqueue(response)
    }
    
    /**
     * Get the last recorded request.
     */
    fun takeRequest(timeout: Long = 1, unit: TimeUnit = TimeUnit.SECONDS): RecordedRequest? {
        return server.takeRequest(timeout, unit)
    }
    
    /**
     * Set a custom dispatcher for advanced scenarios.
     */
    fun setDispatcher(dispatcher: Dispatcher) {
        server.dispatcher = dispatcher
    }
    
    /**
     * Dispatcher that handles all Overseerr API endpoints.
     */
    private inner class OverseerrDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: return errorResponse(404, "Not Found")
            val method = request.method ?: "GET"
            
            return when {
                // Auth endpoints
                path == "/api/v1/auth/plex" && method == "POST" -> successResponse(json.encodeToString(MockResponses.userProfile()))
                path == "/api/v1/auth/me" && method == "GET" -> successResponse(json.encodeToString(MockResponses.userProfile()))
                path == "/api/v1/auth/logout" && method == "POST" -> handleLogout()
                path == "/api/v1/status" && method == "GET" -> successResponse(json.encodeToString(MockResponses.serverInfo()))
                
                // Discovery endpoints
                path.startsWith("/api/v1/discover/trending") -> handleGetTrending(request)
                path.startsWith("/api/v1/discover/movies") -> handleGetTrendingMovies(request)
                path.startsWith("/api/v1/discover/tv") -> handleGetTrendingTvShows(request)
                path.startsWith("/api/v1/search") -> handleSearch(request)
                path.startsWith("/api/v1/movie/") -> handleGetMovieDetails(request)
                path.startsWith("/api/v1/tv/") -> handleGetTvShowDetails(request)
                
                // Request endpoints
                path.startsWith("/api/v1/request") && method == "POST" -> {
                    if (path.contains("mediaId=")) {
                        // Return ApiMediaRequest for requestMovie/requestTvShow
                        successResponse(json.encodeToString(MockResponses.mediaRequest(1)))
                    } else {
                        // Return ApiRequestResponse for submitRequest(@Body)
                        successResponse(json.encodeToString(MockResponses.requestResponse()))
                    }
                }
                path.startsWith("/api/v1/request") && method == "GET" -> handleGetRequests(request)
                path.startsWith("/api/v1/request/") && method == "GET" -> handleGetRequest(request)
                path.startsWith("/api/v1/request/") && method == "DELETE" -> successResponse("", 204)
                path.contains("/requests") && path.contains("/user/") -> handleGetUserRequests(request)
                path.contains("/status") && path.contains("/request/") -> handleGetRequestStatus(request)
                path == "/api/v1/settings/radarr/profiles" -> successResponse(json.encodeToString(MockResponses.qualityProfiles()))
                path == "/api/v1/settings/radarr/folders" -> successResponse(json.encodeToString(MockResponses.rootFolders()))
                
                // User endpoints
                path == "/api/v1/user/quota" -> successResponse(json.encodeToString(MockResponses.userQuota()))
                path == "/api/v1/user/stats" -> successResponse(json.encodeToString(MockResponses.userStatistics()))
                path == "/api/v1/user" -> successResponse(json.encodeToString(MockResponses.userProfile()))
                path.startsWith("/api/v1/user/") && !path.contains("/requests") -> successResponse(json.encodeToString(MockResponses.userProfile()))
                
                else -> errorResponse(404, "Endpoint not found: $path")
            }
        }
        
        
        private fun handleGetTrending(request: RecordedRequest): MockResponse {
            val page = extractQueryParam(request, "page")?.toIntOrNull() ?: 1
            return successResponse(json.encodeToString(MockResponses.searchResults(page)))
        }
        
        private fun handleGetTrendingMovies(request: RecordedRequest): MockResponse {
            val page = extractQueryParam(request, "page")?.toIntOrNull() ?: 1
            return successResponse(json.encodeToString(MockResponses.movieSearchResults(page)))
        }
        
        private fun handleGetTrendingTvShows(request: RecordedRequest): MockResponse {
            val page = extractQueryParam(request, "page")?.toIntOrNull() ?: 1
            return successResponse(json.encodeToString(MockResponses.tvShowSearchResults(page)))
        }
        
        private fun handleSearch(request: RecordedRequest): MockResponse {
            val query = extractQueryParam(request, "query") ?: ""
            val page = extractQueryParam(request, "page")?.toIntOrNull() ?: 1
            return successResponse(json.encodeToString(MockResponses.searchResults(page, query)))
        }
        
        private fun handleGetMovieDetails(request: RecordedRequest): MockResponse {
            val movieId = extractPathId(request.path!!)
            return successResponse(json.encodeToString(MockResponses.movieDetails(movieId)))
        }
        
        private fun handleGetTvShowDetails(request: RecordedRequest): MockResponse {
            val tvId = extractPathId(request.path!!)
            return successResponse(json.encodeToString(MockResponses.tvShowDetails(tvId)))
        }
        
        private fun handleSubmitRequest(request: RecordedRequest): MockResponse {
            return successResponse(json.encodeToString(MockResponses.requestResponse()))
        }
        
        private fun handleGetRequests(request: RecordedRequest): MockResponse {
            val page = extractQueryParam(request, "skip")?.toIntOrNull()?.div(20) ?: 0
            return successResponse(json.encodeToString(MockResponses.requestsList(page + 1)))
        }
        
        private fun handleGetRequest(request: RecordedRequest): MockResponse {
            val requestId = extractPathId(request.path!!)
            return successResponse(json.encodeToString(MockResponses.mediaRequest(requestId)))
        }
        
        private fun handleGetUserRequests(request: RecordedRequest): MockResponse {
            val page = extractQueryParam(request, "skip")?.toIntOrNull()?.div(20) ?: 0
            return successResponse(json.encodeToString(MockResponses.requestsList(page + 1)))
        }
        
        private fun handleGetRequestStatus(request: RecordedRequest): MockResponse {
            val requestId = extractPathId(request.path!!)
            return successResponse(json.encodeToString(MockResponses.requestStatus(requestId)))
        }
        
        private fun successResponse(jsonBody: String, code: Int = 200): MockResponse {
            return MockResponse()
                .setResponseCode(code)
                .setHeader("Content-Type", "application/json")
                .setBody(jsonBody)
        }
        
        private fun errorResponse(code: Int, message: String): MockResponse {
            val error = mapOf("message" to message)
            return MockResponse()
                .setResponseCode(code)
                .setHeader("Content-Type", "application/json")
                .setBody(json.encodeToString(error))
        }
        
        private fun handleLogout(): MockResponse {
            return MockResponse()
                .setResponseCode(204)
                .setHeader("Content-Type", "application/json")
        }
        
        private fun extractQueryParam(request: RecordedRequest, param: String): String? {
            val url = request.requestUrl ?: return null
            return url.queryParameter(param)
        }
        
        private fun extractPathId(path: String): Int {
            val pathWithoutQuery = path.split("?").first()
            return pathWithoutQuery.split("/").lastOrNull()?.toIntOrNull() ?: 1
        }
    }
}
