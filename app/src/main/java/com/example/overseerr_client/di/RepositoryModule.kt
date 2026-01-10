package com.example.overseerr_client.di

import com.example.overseerr_client.data.repository.AuthRepositoryImpl
import com.example.overseerr_client.data.repository.CacheRepositoryImpl
import com.example.overseerr_client.data.repository.DiscoveryRepositoryImpl
import com.example.overseerr_client.data.repository.NotificationRepositoryImpl
import com.example.overseerr_client.data.repository.ProfileRepositoryImpl
import com.example.overseerr_client.data.repository.RequestRepositoryImpl
import com.example.overseerr_client.data.repository.SettingsRepositoryImpl
import com.example.overseerr_client.domain.repository.AuthRepository
import com.example.overseerr_client.domain.repository.CacheRepository
import com.example.overseerr_client.domain.repository.DiscoveryRepository
import com.example.overseerr_client.domain.repository.NotificationRepository
import com.example.overseerr_client.domain.repository.ProfileRepository
import com.example.overseerr_client.domain.repository.RequestRepository
import com.example.overseerr_client.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations.
 * Feature: overseerr-android-client
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    /**
     * Bind AuthRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
    
    /**
     * Bind CacheRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindCacheRepository(
        impl: CacheRepositoryImpl
    ): CacheRepository
    
    /**
     * Bind DiscoveryRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindDiscoveryRepository(
        impl: DiscoveryRepositoryImpl
    ): DiscoveryRepository
    
    /**
     * Bind RequestRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindRequestRepository(
        impl: RequestRepositoryImpl
    ): RequestRepository
    
    /**
     * Bind ProfileRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
    
    /**
     * Bind SettingsRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
    
    /**
     * Bind NotificationRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}
