package gorokhov.stepan.features.chats.controllers.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetMessagesResponse(
    val messages: List<MessageDto>
) 