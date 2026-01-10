package com.example.overseerr_client.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.overseerr_client.data.remote.model.toMovie
import com.example.overseerr_client.data.remote.model.toSearchResults
import com.example.overseerr_client.data.remote.model.toTvShow
import com.example.overseerr_client.data.paging.TrendingMoviesPagingSource
import com.example.overseerr_client.data.paging.TrendingTvShowsPagingSource
import com.example.overseerr_client.data.remote.api.DiscoveryApiService
import com.example.overseerr_client.data.remote.safeApiCall
import com.example.overseerr_client.domain.model.Movie
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.model.SearchResults
import com.example.overseerr_client.domain.model.TvShow
import com.example.overseerr_client.domain.repository.DiscoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of DiscoveryRepository for media discovery operations.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.1, 2.2, 2.4
 */
@Singleton
class DiscoveryRepositoryImpl @Inject constructor(
    private val discoveryApiService: DiscoveryApiService
) : DiscoveryRepository {
    
    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
    }
    
    override fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrendingMoviesPagingSource(discoveryApiService)
            }
        ).flow
    }
    
    override fun getTrendingTvShows(): Flow<PagingData<TvShow>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TrendingTvShowsPagingSource(discoveryApiService)
            }
        ).flow
    }
    
    override suspend fun searchMedia(query: String, page: Int): Result<SearchResults> {
        return safeApiCall {
            val apiSearchResults = discoveryApiService.search(query, page)
            apiSearchResults.toSearchResults()
        }
    }
    
    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val result = safeApiCall {
                discoveryApiService.getMovieDetails(movieId)
            }
            
            when (result) {
                is Result.Success -> {
                    val movie = result.data.toMovie()
                    Result.success(movie)
                }
                is Result.Error -> result
                is Result.Loading -> Result.loading()
            }
        } catch (e: Exception) {
            Result.error(
                com.example.overseerr_client.domain.model.AppError.NetworkError(
                    "Failed to get movie details: ${e.message}"
                )
            )
        }
    }
    
    override suspend fun getTvShowDetails(tvShowId: Int): Result<TvShow> {
        return try {
            val result = safeApiCall {
                discoveryApiService.getTvShowDetails(tvShowId)
            }
            
            when (result) {
                is Result.Success -> {
                    val tvShow = result.data.toTvShow()
                    Result.success(tvShow)
                }
                is Result.Error -> result
                is Result.Loading -> Result.loading()
            }
        } catch (e: Exception) {
            Result.error(
                com.example.overseerr_client.domain.model.AppError.NetworkError(
                    "Failed to get TV show details: ${e.message}"
                )
            )
        }
    }
    
    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        // Similar to trending, but would use a different paging source
        // For now, reusing trending as placeholder
        return getTrendingMovies()
    }
    
    override fun getPopularTvShows(): Flow<PagingData<TvShow>> {
        // Similar to trending, but would use a different paging source
        // For now, reusing trending as placeholder
        return getTrendingTvShows()
    }
}
