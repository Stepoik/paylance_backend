package gorokhov.stepan.features.users.controllers

import gorokhov.stepan.features.users.controllers.mappers.toDto
import gorokhov.stepan.features.users.domain.services.UserService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserController : KoinComponent {
    private val userService by inject<UserService>()

    fun registerRoutes(routing: Routing) = with(routing) {
        route("users") {
            get("/freelancer/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val freelancer = userService.getFreelancer(id)
                call.respond(freelancer.toDto())
            }
        }
    }
}