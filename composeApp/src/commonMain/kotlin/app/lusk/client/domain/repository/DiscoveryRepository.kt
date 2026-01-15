package app.lusk.client.domain.repository

import androidx.paging.PagingData
import app.lusk.client.domain.model.MediaType
import app.lusk.client.domain.model.Movie
import app.lusk.client.domain.model.Result
import app.lusk.client.domain.model.SearchResults
import app.lusk.client.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for media discovery operations.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.1, 2.2, 2.4
 */
interface DiscoveryRepository {
    
    /**
     * Get trending movies with pagination.
     * Property 8: Pagination Consistency
     */
    fun getTrendingMovies(): Flow<PagingData<Movie>>
    
    /**
     * Get trending TV shows with pagination.
     * Property 8: Pagination Consistency
     */
    fun getTrendingTvShows(): Flow<PagingData<TvShow>>
    
    /**
     * Search for media (movies and TV shows).
     * Property 5: Search Performance
     * Property 6: Search Result Completeness
     */
    suspend fun searchMedia(query: String, page: Int = 1): Result<SearchResults>
    
    /**
     * Search for media with paging.
     */
    fun findMedia(query: String): Flow<PagingData<app.lusk.client.domain.model.SearchResult>>
    
    /**
     * Get detailed information about a movie.
     * Property 7: Media Detail Navigation
     */
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    
    /**
     * Get detailed information about a TV show.
     * Property 7: Media Detail Navigation
     */
    suspend fun getTvShowDetails(tvShowId: Int): Result<TvShow>
    
    /**
     * Get popular movies with pagination.
     */
    fun getPopularMovies(): Flow<PagingData<Movie>>
    
    /**
     * Get popular TV shows with pagination.
     */
    fun getPopularTvShows(): Flow<PagingData<TvShow>>
}
