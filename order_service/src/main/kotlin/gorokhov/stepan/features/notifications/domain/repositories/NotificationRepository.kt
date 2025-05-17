package gorokhov.stepan.features.notifications.domain.repositories

import gorokhov.stepan.features.notifications.domain.Notification
import gorokhov.stepan.features.notifications.domain.NotificationType

interface NotificationRepository {
    suspend fun save(notification: Notification)
    suspend fun getByUserId(userId: String, offset: Long, limit: Long): List<Notification>
    suspend fun getCountByUserId(userId: String): Long
} 