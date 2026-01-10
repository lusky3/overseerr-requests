package com.example.overseerr_client.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.overseerr_client.data.local.converter.IntListConverter

/**
 * Room entity for cached media request data.
 */
@Entity(tableName = "user_requests")
@TypeConverters(IntListConverter::class)
data class MediaRequestEntity(
    @PrimaryKey val id: Int,
    val mediaType: String,
    val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val status: Int,
    val requestedDate: Long,
    val seasons: List<Int>?,
    val cachedAt: Long
)
