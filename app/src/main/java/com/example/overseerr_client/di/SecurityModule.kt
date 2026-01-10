package com.example.overseerr_client.di

import com.example.overseerr_client.data.security.SecurityManagerImpl
import com.example.overseerr_client.domain.security.SecurityManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for security-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {
    
    @Binds
    @Singleton
    abstract fun bindSecurityManager(
        securityManagerImpl: SecurityManagerImpl
    ): SecurityManager
}
