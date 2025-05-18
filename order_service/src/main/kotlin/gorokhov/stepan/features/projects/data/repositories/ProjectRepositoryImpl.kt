package gorokhov.stepan.features.projects.data.repositories

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Hit
import co.elastic.clients.json.JsonData
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.elasticsearch.indices.GetIndexRequest
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProjectRepositoryImpl : ProjectRepository, KoinComponent {
    private val elasticClient: ElasticsearchClient by inject()
    private val indexName = "projects"

    init {
        createIndexIfNotExists()
    }

    private fun createIndexIfNotExists() {
        try {
            elasticClient.indices().get(GetIndexRequest.Builder().index(indexName).build())
        } catch (e: Exception) {
            elasticClient.indices().create { req ->
                req.index("projects")
                    .mappings { m ->
                        m.properties("id") { it.keyword { b -> b } }
                            .properties("title") { it.text { b -> b } }
                            .properties("description") { it.text { b -> b } }
                            .properties("budget") { it.double_ { b -> b } }
                            .properties("deadline") { it.date { b -> b.format("strict_date_optional_time||epoch_millis") } }
                            .properties("status") { it.keyword { b -> b } }
                            .properties("createdAt") { it.date { b -> b.format("strict_date_optional_time||epoch_millis") } }
                            .properties("updatedAt") { it.date { b -> b.format("strict_date_optional_time||epoch_millis") } }
                            .properties("ownerId") { it.keyword { b -> b } }
                            .properties("skills") { it.keyword { b -> b } } // ✔️ ключевая строка
                    }
            }
        }
    }

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
        }.also {
            indexProject(it)
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
        }.also {
            indexProject(it)
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

    override suspend fun searchProjects(query: String, offset: Long, limit: Int): List<Project> {
        val searchResponse: SearchResponse<JsonData> = elasticClient.search(
            SearchRequest.Builder()
                .index(indexName)
                .query { q ->
                    q.multiMatch { mm ->
                        mm.query(query)
                            .fields("title^2", "description", "skills")
                    }
                }
                .from(offset.toInt())
                .size(limit)
                .build(),
            JsonData::class.java
        )

        return searchResponse.hits().hits().map { hit: Hit<JsonData> ->
            val projectId = hit.id()
            getProject(projectId) ?: throw IllegalStateException("Project not found in database: $projectId")
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private suspend fun indexProject(project: Project) {
        elasticClient.index { index ->
            index.index(indexName)
                .id(project.id)
                .document(
                    mapOf(
                        "title" to project.title,
                        "description" to project.description,
                        "skills" to project.skills,
                        "status" to project.status.name,
                        "budget" to project.budgetRubles,
                        "deadline" to project.deadline.toString(),
                        "createdAt" to project.createdAt.toString(),
                        "updatedAt" to project.updatedAt.toString(),
                        "ownerId" to project.ownerId
                    )
                )
        }
    }
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