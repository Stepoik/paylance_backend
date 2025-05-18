package gorokhov.stepan.features.chats.controllers.models.responses

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.features.chats.domain.models.Message
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val isRead: Boolean
)

fun Message.toDto(): MessageDto = MessageDto(
    id = id,
    chatId = chatId,
    senderId = senderId,
    text = text,
    createdAt = createdAt,
    isRead = isRead
) 