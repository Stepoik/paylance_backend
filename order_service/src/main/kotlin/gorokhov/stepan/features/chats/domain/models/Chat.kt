package gorokhov.stepan.features.chats.domain.models

import java.time.LocalDateTime

data class Chat(
    val id: String,
    val projectId: String,
    val clientId: String,
    val freelancerId: String,
    val title: String,
    val lastMessage: Message?,
    val unreadCount: Int
)

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean
) 