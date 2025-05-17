package gorokhov.stepan.features.projects.domain.repositories

import gorokhov.stepan.features.projects.domain.models.ProjectResponse

interface ProjectResponseRepository {
    suspend fun createResponse(response: ProjectResponse): ProjectResponse

    suspend fun getResponsesByOwnerId(ownerId: String, offset: Long): List<ProjectResponse>

    suspend fun getResponsesByFreelancerId(userId: String, offset: Long): List<ProjectResponse>

    suspend fun getResponse(id: String): ProjectResponse?

    suspend fun getResponseByProjectAndFreelancerId(projectId: String, freelancerId: String): ProjectResponse?

    suspend fun updateResponse(response: ProjectResponse): ProjectResponse
}