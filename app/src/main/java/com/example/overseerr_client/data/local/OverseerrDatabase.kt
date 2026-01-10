package com.example.overseerr_client.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.overseerr_client.data.local.converter.IntListConverter
import com.example.overseerr_client.data.local.dao.MediaRequestDao
import com.example.overseerr_client.data.local.dao.MovieDao
import com.example.overseerr_client.data.local.dao.NotificationDao
import com.example.overseerr_client.data.local.dao.TvShowDao
import com.example.overseerr_client.data.local.entity.MediaRequestEntity
import com.example.overseerr_client.data.local.entity.MovieEntity
import com.example.overseerr_client.data.local.entity.NotificationEntity
import com.example.overseerr_client.data.local.entity.TvShowEntity

/**
 * Room database for the Overseerr Android Client.
 * Feature: overseerr-android-client
 * Validates: Requirements 7.1, 7.4
 */
@Database(
    entities = [
        MovieEntity::class,
        TvShowEntity::class,
        MediaRequestEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(IntListConverter::class)
abstract class OverseerrDatabase : RoomDatabase() {
    
    /**
     * Get the MovieDao for accessing movie data.
     */
    abstract fun movieDao(): MovieDao
    
    /**
     * Get the TvShowDao for accessing TV show data.
     */
    abstract fun tvShowDao(): TvShowDao
    
    /**
     * Get the MediaRequestDao for accessing media request data.
     */
    abstract fun mediaRequestDao(): MediaRequestDao
    
    /**
     * Get the NotificationDao for accessing notification data.
     */
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        const val DATABASE_NAME = "overseerr_database"
    }
}
