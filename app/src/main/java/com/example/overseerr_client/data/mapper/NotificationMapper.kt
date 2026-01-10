package com.example.overseerr_client.data.mapper

import com.example.overseerr_client.data.local.entity.NotificationEntity
import com.example.overseerr_client.domain.model.Notification
import com.example.overseerr_client.domain.model.NotificationType

/**
 * Extension function to convert NotificationEntity to domain Notification.
 */
fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        title = title,
        body = body,
        type = NotificationType.valueOf(type),
        timestamp = timestamp,
        isRead = isRead,
        deepLink = deepLink,
        mediaId = mediaId,
        requestId = requestId
    )
}

/**
 * Extension function to convert domain Notification to NotificationEntity.
 */
fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        body = body,
        type = type.name,
        timestamp = timestamp,
        isRead = isRead,
        deepLink = deepLink,
        mediaId = mediaId,
        requestId = requestId
    )
}
