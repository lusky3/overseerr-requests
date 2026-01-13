package app.lusk.client.data.remote.api

import app.lusk.client.data.remote.model.ApiMovie
import app.lusk.client.data.remote.model.ApiSearchResults
import app.lusk.client.data.remote.model.ApiTvShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for media discovery endpoints.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.1, 2.2, 2.3, 2.4, 2.5
 */
interface DiscoveryApiService {
    
    /**
     * Get trending movies and TV shows.
     * 
     * @param page Page number for pagination (default: 1)
     * @param language Preferred language for results (default: "en")
     * @return Search results with trending media
     */
    @GET("/api/v1/discover/trending")
    suspend fun getTrending(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): ApiSearchResults
    
    /**
     * Get trending movies.
     * 
     * @param page Page number for pagination (default: 1)
     * @param language Preferred language for results (default: "en")
     * @return Search results with trending movies
     */
    @GET("/api/v1/discover/movies")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): ApiSearchResults
    
    /**
     * Get trending TV shows.
     * 
     * @param page Page number for pagination (default: 1)
     * @param language Preferred language for results (default: "en")
     * @return Search results with trending TV shows
     */
    @GET("/api/v1/discover/tv")
    suspend fun getTrendingTvShows(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): ApiSearchResults
    
    /**
     * Search for movies and TV shows.
     * 
     * @param query Search query string
     * @param page Page number for pagination (default: 1)
     * @param language Preferred language for results (default: "en")
     * @return Search results matching the query
     */
    @GET("/api/v1/search")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en"
    ): ApiSearchResults
    
    /**
     * Get detailed information about a movie.
     * 
     * @param movieId The TMDB ID of the movie
     * @param language Preferred language for results (default: "en")
     * @return Detailed movie information
     */
    @GET("/api/v1/movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en"
    ): ApiMovie
    
    /**
     * Get detailed information about a TV show.
     * 
     * @param tvId The TMDB ID of the TV show
     * @param language Preferred language for results (default: "en")
     * @return Detailed TV show information
     */
    @GET("/api/v1/tv/{tvId}")
    suspend fun getTvShowDetails(
        @Path("tvId") tvId: Int,
        @Query("language") language: String = "en"
    ): ApiTvShow
}
