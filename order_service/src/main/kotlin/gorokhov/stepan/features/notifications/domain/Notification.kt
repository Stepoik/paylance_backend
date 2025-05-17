package gorokhov.stepan.features.notifications.domain

import java.time.LocalDateTime

data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val createdAt: LocalDateTime,
    val projectId: String,
    val responseId: String,
    val isRead: Boolean = false
)

enum class NotificationType {
    PROJECT_RESPONSE, // Отклик на проект
    RESPONSE_RESULT // Результат отклика
}