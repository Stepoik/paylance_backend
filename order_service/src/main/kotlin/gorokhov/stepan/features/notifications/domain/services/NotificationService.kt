package gorokhov.stepan.features.notifications.domain.services

import gorokhov.stepan.features.notifications.domain.Notification
import gorokhov.stepan.features.notifications.domain.NotificationType
import gorokhov.stepan.features.notifications.domain.repositories.NotificationRepository
import gorokhov.stepan.features.projects.domain.models.ReplyType
import java.time.LocalDateTime
import java.util.UUID

class NotificationService(private val repository: NotificationRepository) {
    suspend fun createProjectResponseNotification(
        projectOwnerId: String,
        projectId: String,
        responseId: String,
        freelancerName: String
    ) {
        val notification = Notification(
            id = UUID.randomUUID().toString(),
            userId = projectOwnerId,
            type = NotificationType.PROJECT_RESPONSE,
            title = "Новый отклик на проект",
            message = "Пользователь $freelancerName откликнулся на ваш проект",
            createdAt = LocalDateTime.now(),
            projectId = projectId,
            responseId = responseId,
            isRead = false
        )
        repository.save(notification)
    }

    suspend fun createResponseResultNotification(
        freelancerId: String,
        projectId: String,
        replyType: ReplyType,
        projectTitle: String
    ) {
        val (title, message) = when (replyType) {
            ReplyType.ACCEPT -> "Ваш отклик принят" to "Ваш отклик на проект \"$projectTitle\" был принят"
            ReplyType.REJECT -> "Ваш отклик отклонен" to "Ваш отклик на проект \"$projectTitle\" был отклонен"
        }

        val notification = Notification(
            id = UUID.randomUUID().toString(),
            userId = freelancerId,
            type = NotificationType.RESPONSE_RESULT,
            title = title,
            message = message,
            createdAt = LocalDateTime.now(),
            projectId = projectId,
            responseId = "",
            isRead = false
        )
        repository.save(notification)
    }
} 