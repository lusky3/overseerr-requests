package app.lusk.client.di

import app.lusk.client.data.repository.AuthRepositoryImpl
import app.lusk.client.data.repository.CacheRepositoryImpl
import app.lusk.client.data.repository.DiscoveryRepositoryImpl
import app.lusk.client.data.repository.NotificationRepositoryImpl
import app.lusk.client.data.repository.ProfileRepositoryImpl
import app.lusk.client.data.repository.RequestRepositoryImpl
import app.lusk.client.data.repository.SettingsRepositoryImpl
import app.lusk.client.domain.repository.AuthRepository
import app.lusk.client.domain.repository.CacheRepository
import app.lusk.client.domain.repository.DiscoveryRepository
import app.lusk.client.domain.repository.NotificationRepository
import app.lusk.client.domain.repository.ProfileRepository
import app.lusk.client.domain.repository.RequestRepository
import app.lusk.client.domain.repository.SettingsRepository
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
