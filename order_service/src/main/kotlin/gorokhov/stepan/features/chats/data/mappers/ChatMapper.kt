package gorokhov.stepan.features.chats.data.mappers

import gorokhov.stepan.features.chats.data.database.Chats
import gorokhov.stepan.features.chats.data.database.Messages
import gorokhov.stepan.features.chats.domain.models.Chat
import gorokhov.stepan.features.chats.domain.models.Message
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toMessage(): Message = Message(
    id = this[Messages.id],
    chatId = this[Messages.chatId],
    senderId = this[Messages.senderId],
    text = this[Messages.text],
    createdAt = this[Messages.createdAt],
    isRead = this[Messages.isRead]
)

fun ResultRow.toChat(lastMessage: Message?, unreadCount: Int): Chat = Chat(
    id = this[Chats.id],
    projectId = this[Chats.projectId],
    clientId = this[Chats.clientId],
    freelancerId = this[Chats.freelancerId],
    lastMessage = lastMessage,
    title = this[Chats.title],
    unreadCount = unreadCount
)