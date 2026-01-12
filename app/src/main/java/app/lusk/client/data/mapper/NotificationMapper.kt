package app.lusk.client.data.mapper

import app.lusk.client.data.local.entity.NotificationEntity
import app.lusk.client.domain.model.Notification
import app.lusk.client.domain.model.NotificationType

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
