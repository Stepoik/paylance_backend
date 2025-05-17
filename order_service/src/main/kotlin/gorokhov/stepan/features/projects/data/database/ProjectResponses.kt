package gorokhov.stepan.features.projects.data.database

import gorokhov.stepan.features.projects.domain.models.ResponseStatus
import org.jetbrains.exposed.sql.Table

object ProjectResponses : Table("project_responses") {
    val id = varchar("id", 50)
    val projectId = varchar("project_id", 50)
    val ownerId = varchar("owner_id", 50)
    val freelanceId = varchar("freelance_id", 50)
    val status = enumerationByName("status", 20, ResponseStatus::class)

    override val primaryKey: PrimaryKey get() = PrimaryKey(id)
}