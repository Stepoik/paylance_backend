package gorokhov.stepan.features.projects.domain.services

import gorokhov.stepan.features.projects.domain.exceptions.HttpException
import gorokhov.stepan.features.projects.domain.models.Project
import gorokhov.stepan.features.projects.domain.models.ProjectStatus
import gorokhov.stepan.features.projects.domain.models.ProjectWithAuthor
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import gorokhov.stepan.features.users.domain.UserRepository
import io.ktor.http.*
import io.ktor.server.plugins.*

class ProjectService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) {
    suspend fun createProject(project: Project): ProjectWithAuthor {
        val createdProject = projectRepository.createProject(project)
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        return ProjectWithAuthor(createdProject, user)
    }

    suspend fun updateProject(userId: String, updatedProject: Project): ProjectWithAuthor {
        val oldProject = projectRepository.getProject(updatedProject.id) ?: throw NotFoundException("Project not found")
        if (oldProject.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Project not found")
        if (oldProject.status == ProjectStatus.CANCELLED) throw BadRequestException("Project canceled")

        val project = projectRepository.updateProject(updatedProject)
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        return ProjectWithAuthor(project, user)
    }

    suspend fun closeProject(userId: String, projectId: String) {
        val project = projectRepository.getProject(projectId) ?: throw NotFoundException("Project not found")
        if (project.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Project not found")

        val updatedProject = project.copy(status = ProjectStatus.CANCELLED)
        projectRepository.updateProject(updatedProject)
    }

    suspend fun getProject(id: String): ProjectWithAuthor {
        val project = projectRepository.getProject(id) ?: throw NotFoundException("Project not found")
        val user = userRepository.getUser(project.ownerId) ?: throw NotFoundException("User not found")
        return ProjectWithAuthor(project, user)
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

    private suspend fun getProjectsWithAuthor(provider: suspend () -> List<Project>): List<ProjectWithAuthor> {
        val projects = provider()
        val users = userRepository.getUsers(projects.map { it.ownerId }.toSet().toList()).associateBy { it.id }
        return projects.map { ProjectWithAuthor(project = it, author = users[it.ownerId]!!) }
    }

    companion object {
        const val BASE_LIMIT = 50
    }
}