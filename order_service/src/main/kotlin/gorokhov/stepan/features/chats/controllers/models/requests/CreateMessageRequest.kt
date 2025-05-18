package gorokhov.stepan.features.chats.controllers.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateMessageRequest(
    val text: String
) 