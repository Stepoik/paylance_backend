package gorokhov.stepan.features.chats.domain.repositories

import gorokhov.stepan.features.chats.domain.models.Chat
import gorokhov.stepan.features.chats.domain.models.CreateChat
import gorokhov.stepan.features.chats.domain.models.Message

interface ChatRepository {
    suspend fun createChat(chat: CreateChat)
    suspend fun getChatById(chatId: String): Chat?
    suspend fun getChatsByUserId(userId: String, offset: Long, limit: Long): List<Chat>
    suspend fun getChatByProjectId(projectId: String): Chat?
    suspend fun getMessagesByChatId(chatId: String, offset: Long, limit: Long): List<Message>
    suspend fun createMessage(message: Message): Message
    suspend fun markMessagesAsRead(chatId: String, userId: String)
    suspend fun getUnreadCount(chatId: String, userId: String): Int
} 