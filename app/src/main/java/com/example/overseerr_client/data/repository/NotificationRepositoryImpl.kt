package com.example.overseerr_client.data.repository

import com.example.overseerr_client.data.local.dao.NotificationDao
import com.example.overseerr_client.data.mapper.toDomain
import com.example.overseerr_client.data.mapper.toEntity
import com.example.overseerr_client.data.remote.api.UserApiService
import com.example.overseerr_client.data.remote.safeApiCall
import com.example.overseerr_client.domain.model.Notification
import com.example.overseerr_client.domain.model.Result
import com.example.overseerr_client.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of NotificationRepository.
 * Feature: overseerr-android-client
 * Validates: Requirements 6.1, 6.2, 6.3
 */
@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
    private val userApiService: UserApiService
) : NotificationRepository {
    
    override suspend fun registerForPushNotifications(token: String): Result<Unit> {
        return safeApiCall {
            // In a real implementation, this would call the Overseerr API
            // to register the FCM token
            // For now, we'll just return success
            Unit
        }
    }
    
    override suspend fun unregisterPushNotifications(): Result<Unit> {
        return safeApiCall {
            // In a real implementation, this would call the Overseerr API
            // to unregister the FCM token
            Unit
        }
    }
    
    override fun getNotificationHistory(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications()
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override suspend fun markNotificationAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }
    
    override suspend fun saveNotification(notification: Notification) {
        notificationDao.insertNotification(notification.toEntity())
    }
    
    override suspend fun clearAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
    
    override suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteNotification(notificationId)
    }
}
