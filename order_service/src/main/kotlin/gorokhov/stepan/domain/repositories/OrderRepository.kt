package gorokhov.stepan.domain.repositories

import gorokhov.stepan.domain.models.Order

interface OrderRepository {
    suspend fun createOrder(order: Order): Order

    suspend fun getOrder(id: String): Order?

    suspend fun updateOrder(order: Order): Order

    suspend fun getLastOrders(offset: Long, limit: Int): List<Order>
}