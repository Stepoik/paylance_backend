package gorokhov.stepan.features.notifications.data

import gorokhov.stepan.features.notifications.data.database.Notifications
import gorokhov.stepan.features.notifications.data.mappers.toDomain
import gorokhov.stepan.features.notifications.domain.Notification
import gorokhov.stepan.features.notifications.domain.NotificationType
import gorokhov.stepan.features.notifications.domain.repositories.NotificationRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.concurrent.ConcurrentHashMap

class NotificationRepositoryImpl : NotificationRepository {
    override suspend fun save(notification: Notification) {
        dbQuery {
            Notifications.insert {
                it[id] = notification.id
                it[userId] = notification.userId
                it[type] = notification.type
                it[title] = notification.title
                it[message] = notification.message
                it[createdAt] = notification.createdAt
                it[projectId] = notification.projectId
                it[isRead] = notification.isRead
                it[responseId] = notification.responseId
            }
        }
    }

    override suspend fun getByUserId(userId: String, offset: Long, limit: Long): List<Notification> {
        return dbQuery {
            Notifications.selectAll()
                .where { Notifications.userId eq userId }
                .orderBy(Notifications.createdAt, SortOrder.DESC)
                .limit(limit.toInt())
                .offset(offset)
                .map { it.toDomain() }
        }
    }

    override suspend fun getCountByUserId(userId: String): Long {
        return dbQuery {
            Notifications.selectAll()
                .where { Notifications.userId eq userId }
                .count()
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
} 