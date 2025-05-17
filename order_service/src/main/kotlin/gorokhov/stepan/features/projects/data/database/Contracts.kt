package gorokhov.stepan.features.projects.data.database

import gorokhov.stepan.features.projects.domain.models.ContractStatus
import org.jetbrains.exposed.sql.Table

object Contracts : Table("contracts") {
    val id = varchar("id", 50)
    val projectId = varchar("project_id", 50)
    val freelanceId = varchar("freelance_id", 50)
    val clientId = varchar("client_id", 50)
    val status = enumerationByName("status", 20, ContractStatus::class)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}