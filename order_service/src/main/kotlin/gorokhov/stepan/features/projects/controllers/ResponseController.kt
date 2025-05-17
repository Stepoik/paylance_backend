package gorokhov.stepan.features.projects.controllers

import gorokhov.stepan.configurations.AuthenticatedUser
import gorokhov.stepan.features.projects.controllers.models.*
import gorokhov.stepan.features.projects.domain.services.ResponseService
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ResponseController : KoinComponent {
    private val responseService: ResponseService by inject()

    fun registerRoutes(routing: Routing) = with(routing) {
        route("/responses") {
            authenticate {
                post {
                    val user = call.principal<AuthenticatedUser>()!!
                    val request = call.receive<ProjectResponseRequest>()
                    responseService.responseOnProject(freelancerId = user.id, projectId = request.projectId)
                    call.respond(mapOf("status" to "success"))
                }

                post("/reply") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val request = call.receive<ReplyOnResponseRequest>()
                    responseService.replyToResponse(
                        responseId = request.responseId,
                        replyType = request.replyType.toDomain(),
                        ownerId = user.id
                    )
                    call.respond(mapOf("status" to "success"))
                }

                get("/{id}") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val responseId = call.parameters["id"]!!
                    val response = responseService.getResponseById(responseId)
                    call.respond(response.toDto())
                }

                get("/freelancer") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val offset = call.request.queryParameters["offset"]?.toLong() ?: 0
                    val responses = responseService.getFreelancerResponses(userId = user.id, offset = offset)
                    call.respond(GetResponsesResponse(responses.map { it.toDto() }))
                }

                get("/client") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val offset = call.request.queryParameters["offset"]?.toLong() ?: 0
                    val responses = responseService.getOwnerResponses(userId = user.id, offset = offset)
                    call.respond(GetResponsesResponse(responses.map { it.toDto() }))
                }
            }
        }
    }
}