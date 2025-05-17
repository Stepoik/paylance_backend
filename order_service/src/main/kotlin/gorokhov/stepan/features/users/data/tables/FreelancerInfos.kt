package gorokhov.stepan.features.users.data.tables

import org.jetbrains.exposed.sql.Table

object FreelancerInfos : Table("freelancer_infos") {
    val freelancerId = varchar("freelancer_id", 50)
    val description = text("description")
    val skills = text("skills")

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(freelancerId)
}