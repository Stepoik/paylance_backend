package gorokhov.stepan.features.chats.domain.services

import gorokhov.stepan.features.chats.domain.models.Chat
import gorokhov.stepan.features.chats.domain.models.Message
import gorokhov.stepan.features.chats.domain.repositories.ChatRepository
import gorokhov.stepan.features.projects.domain.repositories.ContractRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import io.ktor.server.plugins.*
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ChatService(
    private val chatRepository: ChatRepository,
    private val projectRepository: ProjectRepository,
    private val contractRepository: ContractRepository
) {
    private val messageFlow = MutableSharedFlow<Message>(replay = 0, extraBufferCapacity = 100)

    suspend fun getChatsByUserId(userId: String, offset: Long, limit: Long): List<Chat> {
        return chatRepository.getChatsByUserId(userId, offset, limit)
    }

    suspend fun getChatById(chatId: String, userId: String): Chat {
        val chat = chatRepository.getChatById(chatId) ?: throw NotFoundException("Chat not found")
        val project = projectRepository.getProject(chat.projectId) ?: throw NotFoundException("Project not found")
        if (userId != project.ownerId && userId != chat.clientId && userId != chat.freelancerId) {
            throw NotFoundException("Chat not found")
        }
        return chat
    }

    suspend fun getMessagesByChatId(chatId: String, userId: String, offset: Long, limit: Long): List<Message> {
        val chat = chatRepository.getChatById(chatId) ?: throw NotFoundException("Chat not found")
        if (userId != chat.clientId && userId != chat.freelancerId) {
            throw NotFoundException("Chat not found")
        }
        return chatRepository.getMessagesByChatId(chatId, offset, limit)
    }

    suspend fun createMessage(chatId: String, senderId: String, text: String): Message {
        val chat = chatRepository.getChatById(chatId) ?: throw NotFoundException("Chat not found")
        if (senderId != chat.clientId && senderId != chat.freelancerId) {
            throw NotFoundException("Chat not found")
        }

        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            text = text,
            createdAt = LocalDateTime.now(),
            isRead = false
        )
        val savedMessage = chatRepository.createMessage(message)
        messageFlow.emit(savedMessage)
        return savedMessage
    }

    suspend fun markMessagesAsRead(chatId: String, userId: String) {
        val chat = chatRepository.getChatById(chatId) ?: throw NotFoundException("Chat not found")
        if (userId != chat.clientId && userId != chat.freelancerId) {
            throw NotFoundException("Chat not found")
        }
        chatRepository.markMessagesAsRead(chatId, userId)
    }

    suspend fun subscribeToChat(chatId: String, userId: String, onMessage: suspend (Message) -> Unit) {
        val chat = getChatById(chatId, userId)
        messageFlow.collect { message ->
            if (message.chatId == chat.id) {
                onMessage(message)
            }
        }
    }

    suspend fun unsubscribeFromChat(chatId: String, userId: String) {
        // В текущей реализации не требуется дополнительная логика
        // так как мы используем SharedFlow, который автоматически очищает подписки
    }
} 