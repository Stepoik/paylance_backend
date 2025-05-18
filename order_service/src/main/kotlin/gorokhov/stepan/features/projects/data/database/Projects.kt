package gorokhov.stepan.features.projects.data.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Projects : Table() {
    val id = varchar("id", 36)
    val title = varchar("title", 255)
    val description = text("description")
    val budget = double("budget")
    val deadline = datetime("deadline")
    val status = varchar("status", 20)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val ownerId = varchar("owner_id", 36)
    val skills = text("skills")

    override val primaryKey = PrimaryKey(id)
}