package gorokhov.stepan.features.chats.domain.models

data class CreateChat(
    val id: String,
    val projectId: String,
    val clientId: String,
    val freelancerId: String,
    val title: String,
)
