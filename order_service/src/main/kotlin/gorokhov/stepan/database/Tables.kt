package gorokhov.stepan.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Orders : Table() {
    val id = varchar("id", 36)
    val title = varchar("title", 255)
    val description = text("description")
    val budget = double("budget")
    val deadline = datetime("deadline")
    val status = varchar("status", 20)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val ownerId = varchar("owner_id", 36)

    override val primaryKey = PrimaryKey(id)
}

object OrderResponses : Table() {
    val id = varchar("id", 36)
    val orderId = varchar("order_id", 36)
    val freelancerId = varchar("freelancer_id", 36)
    val proposal = text("proposal")
    val price = double("price")
    val status = varchar("status", 20)
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
} 