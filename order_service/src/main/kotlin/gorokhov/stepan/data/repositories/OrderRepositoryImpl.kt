package gorokhov.stepan.data.repositories

import gorokhov.stepan.database.Orders
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import gorokhov.stepan.domain.repositories.OrderRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class OrderRepositoryImpl: OrderRepository {
    override suspend fun createOrder(order: Order): Order {
        return dbQuery {
            Orders.insert {
                it[id] = order.id
                it[title] = order.title
                it[description] = order.description
                it[budget] = order.budget
                it[deadline] = order.deadline
                it[status] = order.status.name
                it[createdAt] = order.createdAt
                it[updatedAt] = order.updatedAt
                it[ownerId] = order.ownerId
            }
            order
        }
    }

    override suspend fun getOrder(id: String): Order? {
        return dbQuery {
            Orders.selectAll()
                .where { Orders.id eq id }
                .map { row ->
                    row.toOrder()
                }
                .firstOrNull()
        }
    }

    override suspend fun updateOrder(order: Order): Order {
        return dbQuery {
            Orders.update(where = { Orders.id eq order.id }) {
                it[title] = order.title
                it[description] = order.description
                it[budget] = order.budget
                it[deadline] = order.deadline
                it[status] = order.status.name
                it[createdAt] = order.createdAt
                it[updatedAt] = order.updatedAt
                it[ownerId] = order.ownerId
            }
            order
        }
    }

    override suspend fun getLastOrders(offset: Long, limit: Int): List<Order> {
        return dbQuery {
            Orders.selectAll()
                .orderBy(Orders.updatedAt to SortOrder.DESC)
                .limit(limit)
                .offset(offset)
                .map { it.toOrder() }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

private fun ResultRow.toOrder(): Order = Order(
    id = this[Orders.id],
    title = this[Orders.title],
    description = this[Orders.description],
    budget = this[Orders.budget],
    deadline = this[Orders.deadline],
    status = OrderStatus.valueOf(this[Orders.status]),
    createdAt = this[Orders.createdAt],
    updatedAt = this[Orders.updatedAt],
    ownerId = this[Orders.ownerId]
)