package gorokhov.stepan.features.chats.data.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Chats : Table() {
    val id = varchar("id", 36)
    val projectId = varchar("project_id", 36)
    val clientId = varchar("client_id", 36)
    val freelancerId = varchar("freelancer_id", 36)
    val title = varchar("title", 255)

    override val primaryKey = PrimaryKey(id)
}

object Messages : Table() {
    val id = varchar("id", 36)
    val chatId = varchar("chat_id", 36).references(Chats.id)
    val senderId = varchar("sender_id", 36)
    val text = text("text")
    val createdAt = datetime("created_at")
    val isRead = bool("is_read").default(false)

    override val primaryKey = PrimaryKey(id)
}