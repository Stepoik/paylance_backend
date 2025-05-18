package gorokhov.stepan.features.chats.controllers.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetChatsResponse(
    val chats: List<ChatDto>
) 