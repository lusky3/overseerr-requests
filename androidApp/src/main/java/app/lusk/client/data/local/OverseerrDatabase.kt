package app.lusk.client.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.lusk.client.data.local.converter.IntListConverter
import app.lusk.client.data.local.dao.MediaRequestDao
import app.lusk.client.data.local.dao.MovieDao
import app.lusk.client.data.local.dao.NotificationDao
import app.lusk.client.data.local.dao.OfflineRequestDao
import app.lusk.client.data.local.dao.TvShowDao
import app.lusk.client.data.local.entity.MediaRequestEntity
import app.lusk.client.data.local.entity.MovieEntity
import app.lusk.client.data.local.entity.NotificationEntity
import app.lusk.client.data.local.entity.OfflineRequestEntity
import app.lusk.client.data.local.entity.TvShowEntity

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
        NotificationEntity::class,
        OfflineRequestEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(IntListConverter::class)
abstract class OverseerrDatabase : RoomDatabase() {
    
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun mediaRequestDao(): MediaRequestDao
    abstract fun notificationDao(): NotificationDao
    abstract fun offlineRequestDao(): OfflineRequestDao
    
    companion object {
        const val DATABASE_NAME = "overseerr_database"
    }
}
