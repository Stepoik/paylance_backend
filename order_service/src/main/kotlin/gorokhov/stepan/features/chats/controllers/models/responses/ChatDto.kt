package gorokhov.stepan.features.chats.controllers.models.responses

import gorokhov.stepan.features.chats.domain.models.Chat
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id: String,
    val projectId: String,
    val clientId: String,
    val freelancerId: String,
    val title: String,
    val lastMessage: MessageDto?,
    val unreadCount: Int
)

fun Chat.toDto(): ChatDto = ChatDto(
    id = id,
    projectId = projectId,
    clientId = clientId,
    freelancerId = freelancerId,
    title = title,
    lastMessage = lastMessage?.toDto(),
    unreadCount = unreadCount
) 