package gorokhov.stepan.features.notifications.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.features.notifications.domain.NotificationType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NotificationDto(
    val id: String,
    val userId: String,
    val projectId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val responseId: String,
    val isRead: Boolean = false
)