package gorokhov.stepan.features.projects.data.repositories

import gorokhov.stepan.features.projects.data.database.ProjectResponses
import gorokhov.stepan.features.projects.data.mappers.toProjectResponse
import gorokhov.stepan.features.projects.domain.models.ProjectResponse
import gorokhov.stepan.features.projects.domain.repositories.ProjectResponseRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class ProjectResponseRepositoryImpl : ProjectResponseRepository {
    override suspend fun createResponse(response: ProjectResponse): ProjectResponse = dbQuery {
        ProjectResponses.insert {
            it[id] = response.id
            it[projectId] = response.projectId
            it[ownerId] = response.ownerId
            it[freelanceId] = response.freelanceId
            it[status] = response.status
        }
        response
    }

    override suspend fun getResponsesByOwnerId(ownerId: String, offset: Long): List<ProjectResponse> = dbQuery {
        ProjectResponses
            .selectAll()
            .where { ProjectResponses.ownerId eq ownerId }
            .limit(20)
            .offset(offset)
            .map { it.toProjectResponse() }
    }

    override suspend fun getResponsesByFreelancerId(userId: String, offset: Long): List<ProjectResponse> = dbQuery {
        ProjectResponses
            .selectAll()
            .where { ProjectResponses.freelanceId eq userId }
            .limit(20)
            .offset(offset)
            .map { it.toProjectResponse() }
    }

    override suspend fun getResponse(id: String) = dbQuery {
        ProjectResponses
            .selectAll()
            .where { ProjectResponses.id eq id }
            .map { it.toProjectResponse() }
            .singleOrNull()
    }

    override suspend fun getResponseByProjectAndFreelancerId(
        projectId: String,
        freelancerId: String
    ): ProjectResponse? = dbQuery {
        ProjectResponses
            .selectAll()
            .where {
                (ProjectResponses.projectId eq projectId) and
                        (ProjectResponses.freelanceId eq freelancerId)
            }
            .map { it.toProjectResponse() }
            .singleOrNull()
    }

    override suspend fun updateResponse(response: ProjectResponse): ProjectResponse = dbQuery {
        ProjectResponses.update({ ProjectResponses.id eq response.id }) {
            it[projectId] = response.projectId
            it[ownerId] = response.ownerId
            it[freelanceId] = response.freelanceId
            it[status] = response.status
        }
        response
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}