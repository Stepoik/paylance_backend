package gorokhov.stepan.domain.services

import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderWithAuthor
import gorokhov.stepan.domain.repositories.OrderSearchRepository
import gorokhov.stepan.domain.repositories.UserRepository
import org.koin.core.component.KoinComponent

class OrderSearchService(
    private val searchRepository: OrderSearchRepository,
    private val userRepository: UserRepository
) : KoinComponent {
    suspend fun searchOrder(text: String): List<OrderWithAuthor> {
        val orders = searchRepository.searchOrder(text)
        val users = userRepository.getUsers(orders.map { it.ownerId }.toSet().toList()).associateBy { it.id }
        return orders.map { OrderWithAuthor(order = it, author = users[it.ownerId] ?: throw Exception("User not found")) }
    }

    suspend fun updateOrder(order: Order) {
        return searchRepository.indexOrder(order)
    }

    suspend fun createOrder(order: Order) {
        return searchRepository.indexOrder(order)
    }
} 