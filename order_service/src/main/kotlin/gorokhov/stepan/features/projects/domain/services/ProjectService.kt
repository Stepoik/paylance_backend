package gorokhov.stepan.features.projects.domain.services

import gorokhov.stepan.features.projects.domain.exceptions.HttpException
import gorokhov.stepan.features.projects.domain.models.GeneratedDescription
import gorokhov.stepan.features.projects.domain.models.Project
import gorokhov.stepan.features.projects.domain.models.ProjectStatus
import gorokhov.stepan.features.projects.domain.models.ProjectWithAuthor
import gorokhov.stepan.features.projects.domain.repositories.DescriptionGenerator
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectResponseRepository
import gorokhov.stepan.features.users.domain.UserRepository
import io.ktor.http.*
import io.ktor.server.plugins.*

class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val responseRepository: ProjectResponseRepository,
    private val descriptionGenerator: DescriptionGenerator
) {
    suspend fun createProject(project: Project): ProjectWithAuthor {
        val createdProject = projectRepository.createProject(project)
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        return ProjectWithAuthor(createdProject, user, isRespond = false)
    }

    suspend fun updateProject(userId: String, updatedProject: Project): ProjectWithAuthor {
        val oldProject = projectRepository.getProject(updatedProject.id) ?: throw NotFoundException("Project not found")
        if (oldProject.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Project not found")
        if (oldProject.status == ProjectStatus.CANCELLED) throw BadRequestException("Project canceled")

        val project = projectRepository.updateProject(updatedProject)
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        return ProjectWithAuthor(project, user, false)
    }

    suspend fun closeProject(userId: String, projectId: String) {
        val project = projectRepository.getProject(projectId) ?: throw NotFoundException("Project not found")
        if (project.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Project not found")

        val updatedProject = project.copy(status = ProjectStatus.CANCELLED)
        projectRepository.updateProject(updatedProject)
    }

    suspend fun getProject(projectId: String, freelancerId: String): ProjectWithAuthor {
        val project = projectRepository.getProject(projectId) ?: throw NotFoundException("Project not found")
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        val isRespond = responseRepository.getResponseByProjectAndFreelancerId(
            projectId = projectId,
            freelancerId = freelancerId
        ) != null
        return ProjectWithAuthor(project, user, isRespond)
    }

    suspend fun getProjects(offset: Long, limit: Int): List<ProjectWithAuthor> {
        return getProjectsWithAuthor {
            projectRepository.getLastProjects(offset = offset, limit = limit)
        }
    }

    suspend fun getClientProject(userId: String, offset: Long): List<ProjectWithAuthor> {
        return getProjectsWithAuthor {
            projectRepository.getProjectsByOwnerId(offset = offset, limit = BASE_LIMIT, ownerId = userId)
        }
    }

    suspend fun getFreelancerProjects(userId: String, offset: Long): List<ProjectWithAuthor> {
        return getProjectsWithAuthor {
            projectRepository.getProjectsByFreelancerId(offset = offset, limit = BASE_LIMIT, freelancerId = userId)
        }
    }

    suspend fun searchProjects(query: String, offset: Long, limit: Int): List<ProjectWithAuthor> {
        return getProjectsWithAuthor {
            projectRepository.searchProjects(query = query, offset = offset, limit = limit)
        }
    }

    suspend fun generateDescription(prompt: String): GeneratedDescription {
        return descriptionGenerator.generateDescription(prompt) ?: throw HttpException(HttpStatusCode.ServiceUnavailable, "Generating error")
    }

    private suspend fun getProjectsWithAuthor(freelancerId: String? = null, provider: suspend () -> List<Project>): List<ProjectWithAuthor> {
        val projects = provider()
        val users = userRepository.getUsers(projects.map { it.ownerId }.toSet().toList()).associateBy { it.id }

        return projects.map { project ->
            val isRespond = freelancerId?.let {
                responseRepository.getResponseByProjectAndFreelancerId(
                    projectId = project.id,
                    freelancerId = freelancerId
                ) != null
            } ?: false
            ProjectWithAuthor(project = project, author = users[project.ownerId]!!, isRespond = isRespond)
        }
    }

    companion object {
        const val BASE_LIMIT = 50
    }
}