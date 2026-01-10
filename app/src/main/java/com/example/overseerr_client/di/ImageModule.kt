package com.example.overseerr_client.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okio.Path.Companion.toOkioPath
import javax.inject.Singleton

/**
 * Hilt module for image loading configuration.
 * Feature: overseerr-android-client
 * Validates: Requirements 10.2
 * Property 36: Progressive Image Loading
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageModule {
    
    /**
     * Provides configured Coil ImageLoader with OkHttp integration.
     */
    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(coil3.network.okhttp.OkHttpNetworkFetcherFactory(okHttpClient))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25) // Use 25% of app memory for image cache
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache").toOkioPath())
                    .maxSizeBytes(100 * 1024 * 1024) // 100 MB disk cache
                    .build()
            }
            .crossfade(true) // Enable crossfade animation
            .logger(DebugLogger())
            .build()
    }
}
