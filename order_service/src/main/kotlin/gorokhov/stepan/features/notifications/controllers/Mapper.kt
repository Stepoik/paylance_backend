package gorokhov.stepan.features.notifications.controllers

import gorokhov.stepan.features.notifications.controllers.models.NotificationDto
import gorokhov.stepan.features.notifications.domain.Notification

fun Notification.toDto(): NotificationDto {
    return NotificationDto(
        id = id,
        projectId = projectId,
        title = title,
        type = type,
        userId = userId,
        message = message,
        createdAt = createdAt,
        responseId = responseId,
        isRead = isRead
    )
}