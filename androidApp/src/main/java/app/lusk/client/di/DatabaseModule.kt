package app.lusk.client.di

import android.content.Context
import androidx.room.Room
import app.lusk.client.data.local.OverseerrDatabase
import app.lusk.client.data.local.dao.MediaRequestDao
import app.lusk.client.data.local.dao.MovieDao
import app.lusk.client.data.local.dao.TvShowDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 * Feature: overseerr-android-client
 * Validates: Requirements 7.1, 7.4
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provide the Room database instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): OverseerrDatabase {
        return Room.databaseBuilder(
            context,
            OverseerrDatabase::class.java,
            OverseerrDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development - remove in production
            .build()
    }
    
    /**
     * Provide the MovieDao.
     */
    @Provides
    @Singleton
    fun provideMovieDao(database: OverseerrDatabase): MovieDao {
        return database.movieDao()
    }
    
    /**
     * Provide the TvShowDao.
     */
    @Provides
    @Singleton
    fun provideTvShowDao(database: OverseerrDatabase): TvShowDao {
        return database.tvShowDao()
    }
    
    /**
     * Provide the MediaRequestDao.
     */
    @Provides
    @Singleton
    fun provideMediaRequestDao(database: OverseerrDatabase): MediaRequestDao {
        return database.mediaRequestDao()
    }

    @Provides
    @Singleton
    fun provideOfflineRequestDao(database: OverseerrDatabase): app.lusk.client.data.local.dao.OfflineRequestDao {
        return database.offlineRequestDao()
    }
}
