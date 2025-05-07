package gorokhov.stepan.domain.repositories

import gorokhov.stepan.domain.models.Order

interface OrderSearchRepository {
    suspend fun indexOrder(order: Order)

    suspend fun searchOrder(text: String): List<Order>
}