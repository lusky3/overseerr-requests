package com.example.overseerr_client.domain.repository

import androidx.paging.PagingData
import com.example.overseerr_client.domain.model.MediaType
import com.example.overseerr_client.domain.model.Movie
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.model.SearchResults
import com.example.overseerr_client.domain.model.TvShow
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
