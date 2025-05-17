package gorokhov.stepan.features.notifications.controllers

import gorokhov.stepan.configurations.AuthenticatedUser
import gorokhov.stepan.features.notifications.controllers.models.responses.GetNotificationsResponse
import gorokhov.stepan.features.notifications.domain.NotificationType
import gorokhov.stepan.features.notifications.domain.repositories.NotificationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class NotificationController(private val repository: NotificationRepository) {
    fun registerRoutes(routing: Routing) {
        with(routing) {
            route("/notifications") {
                authenticate {
                    get {
                        val user = call.principal<AuthenticatedUser>()!!
                        val offset = call.request.queryParameters["offset"]?.toLong() ?: 0
                        val notifications =
                            repository.getByUserId(userId = user.id, offset = offset, limit = BASE_LIMIT)
                                .map { it.toDto() }
                        val count = repository.getCountByUserId(user.id)
                        val response = GetNotificationsResponse(notifications = notifications, total = count)
                        call.respond(response)
                    }
                }
            }
        }
    }

    companion object {
        private const val BASE_LIMIT = 50L
    }
} 