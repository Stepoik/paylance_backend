package gorokhov.stepan.features.projects.domain.repositories

import gorokhov.stepan.features.projects.domain.models.Project

interface ProjectRepository {
    suspend fun createProject(project: Project): Project

    suspend fun getProject(id: String): Project?

    suspend fun updateProject(project: Project): Project

    suspend fun getLastProjects(offset: Long, limit: Int): List<Project>

    suspend fun getProjectsByOwnerId(offset: Long, limit: Int, ownerId: String): List<Project>

    suspend fun getProjectsByFreelancerId(offset: Long, limit: Int, freelancerId: String): List<Project>

    suspend fun searchProjects(query: String, offset: Long, limit: Int): List<Project>
}