package com.example.overseerr_client.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.overseerr_client.data.remote.model.toTvShow
import com.example.overseerr_client.data.remote.api.DiscoveryApiService
import com.example.overseerr_client.domain.model.TvShow
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource for trending TV shows.
 * Feature: overseerr-android-client
 * Validates: Requirements 2.1, 2.5
 */
class TrendingTvShowsPagingSource(
    private val discoveryApiService: DiscoveryApiService
) : PagingSource<Int, TvShow>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShow> {
        return try {
            val page = params.key ?: 1
            val response = discoveryApiService.getTrendingTvShows(page)
            
            val tvShows = response.results.map { it.toTvShow() }
            
            LoadResult.Page(
                data = tvShows,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, TvShow>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
