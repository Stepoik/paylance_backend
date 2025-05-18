package gorokhov.stepan.features.chats.controllers

import gorokhov.stepan.configurations.AuthenticatedUser
import gorokhov.stepan.features.chats.controllers.models.requests.CreateMessageRequest
import gorokhov.stepan.features.chats.controllers.models.responses.*
import gorokhov.stepan.features.chats.domain.services.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.serialization.json.Json

class ChatController(private val chatService: ChatService) {
    fun registerRoutes(routing: Routing) {
        with(routing) {
            route("/chats") {
                authenticate {
                    get {
                        val user = call.principal<AuthenticatedUser>()!!
                        val offset = call.request.queryParameters["offset"]?.toLong() ?: 0
                        val chats = chatService.getChatsByUserId(user.id, offset, BASE_LIMIT)
                        call.respond(GetChatsResponse(chats.map { it.toDto() }))
                    }

                    get("/{chatId}") {
                        val user = call.principal<AuthenticatedUser>()!!
                        val chatId = call.parameters["chatId"] ?: return@get call.respondText(
                            "Project ID is required",
                            status = HttpStatusCode.BadRequest
                        )
                        val chat = chatService.getChatById(chatId, user.id)
                        call.respond(chat.toDto())
                    }

                    get("/{chatId}/messages") {
                        val user = call.principal<AuthenticatedUser>()!!
                        val chatId = call.parameters["chatId"] ?: return@get call.respondText(
                            "Project ID is required",
                            status = HttpStatusCode.BadRequest
                        )
                        val offset = call.request.queryParameters["offset"]?.toLong() ?: 0
                        val chat = chatService.getChatById(chatId, user.id)
                        val messages = chatService.getMessagesByChatId(chat.id, user.id, offset, BASE_LIMIT)
                        call.respond(GetMessagesResponse(messages.map { it.toDto() }))
                    }

                    post("/{chatId}/messages") {
                        val user = call.principal<AuthenticatedUser>()!!
                        val chatId = call.parameters["chatId"] ?: return@post call.respondText(
                            "Project ID is required",
                            status = HttpStatusCode.BadRequest
                        )
                        val request = call.receive<CreateMessageRequest>()
                        val chat = chatService.getChatById(chatId, user.id)
                        val message = chatService.createMessage(chat.id, user.id, request.text)
                        call.respond(HttpStatusCode.Created, message.toDto())
                    }

                    post("/{chatId}/read") {
                        val user = call.principal<AuthenticatedUser>()!!
                        val chatId = call.parameters["chatId"] ?: return@post call.respondText(
                            "Project ID is required",
                            status = HttpStatusCode.BadRequest
                        )
                        val chat = chatService.getChatById(chatId, user.id)
                        chatService.markMessagesAsRead(chat.id, user.id)
                        call.respond(HttpStatusCode.NoContent)
                    }

                    webSocket("/{chatId}/ws") {
                        val user = call.principal<AuthenticatedUser>()!!
                        val chatId = call.parameters["chatId"] ?: run {
                            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Chat ID is required"))
                            return@webSocket
                        }

                        try {
                            val chat = chatService.getChatById(chatId, user.id)
                            chatService.subscribeToChat(chat.id, user.id) { message ->
                                try {
                                    send(Frame.Text(Json.encodeToString(MessageDto.serializer(), message.toDto())))
                                } catch (e: ClosedSendChannelException) {
                                    chatService.unsubscribeFromChat(chat.id, user.id)
                                    throw e
                                }
                            }

                            for (frame in incoming) {
                                when (frame) {
                                    is Frame.Close -> break
                                    else -> {} // Игнорируем входящие сообщения
                                }
                            }
                        } catch (e: Exception) {
                            close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, e.message ?: "Internal server error"))
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val BASE_LIMIT = 50L
    }
} 