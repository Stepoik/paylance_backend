package gorokhov.stepan.features.notifications.data.mappers

import gorokhov.stepan.features.notifications.data.database.Notifications
import gorokhov.stepan.features.notifications.domain.Notification
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toDomain(): Notification {
    return Notification(
        id = this[Notifications.id],
        userId = this[Notifications.userId],
        type = this[Notifications.type],
        title = this[Notifications.title],
        message = this[Notifications.message],
        createdAt = this[Notifications.createdAt],
        projectId = this[Notifications.projectId],
        isRead = this[Notifications.isRead],
        responseId = this[Notifications.responseId]
    )
}