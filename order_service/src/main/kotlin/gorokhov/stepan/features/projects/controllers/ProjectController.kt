package gorokhov.stepan.features.projects.controllers

import gorokhov.stepan.configurations.AuthenticatedUser
import gorokhov.stepan.features.projects.domain.services.ProjectService
import gorokhov.stepan.features.projects.controllers.models.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProjectController : KoinComponent {
    private val projectService: ProjectService by inject()

    fun registerRoutes(routing: Routing) = with(routing) {
        route("/projects") {
            authenticate {
                post {
                    val user = call.principal<AuthenticatedUser>()!!
                    val project = call.receive<CreateProjectRequest>()
                    val createdProject = projectService.createProject(project = project.toModel(user.id))
                    call.respond(HttpStatusCode.Created, createdProject.toDto())
                }

                put("/{id}") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val projectId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val request = call.receive<UpdateProjectRequest>()
                    val updatedProject = request.toModel(projectId)
                    projectService.updateProject(userId = user.id, updatedProject = updatedProject)
                }

                put("/{id}/close") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val projectId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    projectService.closeProject(userId = user.id, projectId = projectId)
                }

                get("/freelancer") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val offset = call.parameters["offset"]?.toLongOrNull() ?: 0L
                    val projects = projectService.getFreelancerProjects(userId = user.id, offset = offset)
                    call.respond(GetProjectsResponse(projects.map { it.toDto() }))
                }

                get("/client") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val offset = call.parameters["offset"]?.toLongOrNull() ?: 0L
                    val projects = projectService.getClientProject(userId = user.id, offset = offset)
                    call.respond(GetProjectsResponse(projects.map { it.toDto() }))
                }
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val project = projectService.getProject(id).toDto()
                call.respond(project)
            }

            get {
                val offset = call.parameters["offset"]?.toLongOrNull() ?: 0L
                val limit = call.parameters["limit"]?.toInt() ?: DEFAULT_LIMIT
                val projects = projectService.getProjects(offset = offset, limit = limit)
                call.respond(GetProjectsResponse(projects.map { it.toDto() }))
            }
        }
    }

    companion object {
        private const val DEFAULT_LIMIT = 50
    }
}