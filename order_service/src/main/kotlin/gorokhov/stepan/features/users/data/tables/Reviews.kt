package gorokhov.stepan.features.users.data.tables

import gorokhov.stepan.features.users.domain.models.UserType
import org.jetbrains.exposed.sql.Table

object Reviews : Table("reviews") {
    val id = varchar("id", 50)
    val userId = varchar("user_id", 50)
    val title = varchar("title", 255)
    val text = text("text")
    val rate = integer("rate")
    val userType = enumerationByName("user_type", 20, UserType::class)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}