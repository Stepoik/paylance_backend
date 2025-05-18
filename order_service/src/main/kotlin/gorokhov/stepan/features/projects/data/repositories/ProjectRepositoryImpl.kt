package gorokhov.stepan.features.projects.data.repositories

import gorokhov.stepan.features.projects.data.database.Contracts
import gorokhov.stepan.features.projects.data.database.Projects
import gorokhov.stepan.features.projects.domain.models.Project
import gorokhov.stepan.features.projects.domain.models.ProjectStatus
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ProjectRepositoryImpl: ProjectRepository {
    override suspend fun createProject(project: Project): Project {
        return dbQuery {
            Projects.insert {
                it[id] = project.id
                it[title] = project.title
                it[description] = project.description
                it[budget] = project.budgetRubles
                it[deadline] = project.deadline
                it[status] = project.status.name
                it[createdAt] = project.createdAt
                it[updatedAt] = project.updatedAt
                it[ownerId] = project.ownerId
                it[skills] = Json.encodeToString(project.skills)
            }
            project
        }
    }

    override suspend fun getProject(id: String): Project? {
        return dbQuery {
            Projects.selectAll()
                .where { Projects.id eq id }
                .map { row ->
                    row.toProject()
                }
                .firstOrNull()
        }
    }

    override suspend fun updateProject(project: Project): Project {
        return dbQuery {
            Projects.update(where = { Projects.id eq project.id }) {
                it[title] = project.title
                it[description] = project.description
                it[budget] = project.budgetRubles
                it[deadline] = project.deadline
                it[status] = project.status.name
                it[createdAt] = project.createdAt
                it[updatedAt] = project.updatedAt
                it[ownerId] = project.ownerId
                it[skills] = Json.encodeToString(project.skills)
            }
            project
        }
    }

    override suspend fun getLastProjects(offset: Long, limit: Int): List<Project> {
        return dbQuery {
            Projects.selectAll()
                .orderBy(Projects.updatedAt to SortOrder.DESC)
                .limit(limit)
                .offset(offset)
                .map { it.toProject() }
        }
    }

    override suspend fun getProjectsByOwnerId(offset: Long, limit: Int, ownerId: String): List<Project> {
        return dbQuery {
            Projects.selectAll()
                .where { Projects.ownerId eq ownerId }
                .orderBy(Projects.updatedAt to SortOrder.DESC)
                .limit(limit)
                .offset(offset)
                .map { it.toProject() }
        }
    }

    override suspend fun getProjectsByFreelancerId(offset: Long, limit: Int, freelancerId: String): List<Project> {
        return dbQuery {
            Projects.innerJoin(Contracts, { id }, { projectId })
                .selectAll()
                .where { Contracts.freelanceId eq freelancerId }
                .orderBy(Projects.updatedAt to SortOrder.DESC)
                .limit(limit)
                .offset(offset)
                .map { row -> row.toProject() }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun ResultRow.toProject(): Project = Project(
    id = this[Projects.id],
    title = this[Projects.title],
    description = this[Projects.description],
    budgetRubles = this[Projects.budget],
    deadline = this[Projects.deadline],
    status = ProjectStatus.valueOf(this[Projects.status]),
    createdAt = this[Projects.createdAt],
    updatedAt = this[Projects.updatedAt],
    ownerId = this[Projects.ownerId],
    skills = Json.decodeFromString(this[Projects.skills])
)