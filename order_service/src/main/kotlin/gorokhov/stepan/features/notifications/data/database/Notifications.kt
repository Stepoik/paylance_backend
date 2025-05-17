package gorokhov.stepan.features.notifications.data.database

import gorokhov.stepan.features.notifications.domain.NotificationType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Notifications : Table() {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36)
    val type = enumerationByName("type", 50, NotificationType::class)
    val title = varchar("title", 255)
    val message = text("message")
    val createdAt = datetime("created_at")
    val projectId = varchar("project_id", 36)
    val responseId = varchar("response_id", 36)
    val isRead = bool("is_read").default(false)

    override val primaryKey = PrimaryKey(id)
}