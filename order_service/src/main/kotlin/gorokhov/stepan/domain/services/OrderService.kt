package gorokhov.stepan.domain.services

import gorokhov.stepan.domain.exceptions.HttpException
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import gorokhov.stepan.domain.models.OrderWithAuthor
import gorokhov.stepan.domain.repositories.OrderRepository
import gorokhov.stepan.domain.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*

class OrderService(
    private val application: Application,
    private val orderRepository : OrderRepository,
    private val userRepository: UserRepository,
    private val rabbitMQService: RabbitMQService
) {
    suspend fun createOrder(order: Order): OrderWithAuthor {
        val createdOrder = orderRepository.createOrder(order)
        rabbitMQService.publishOrderCreated(application, createdOrder)
        val user = userRepository.getUser(order.ownerId) ?: throw NotFoundException("User not found")
        return OrderWithAuthor(createdOrder, user)
    }

    suspend fun updateOrder(userId: String, updatedOrder: Order): OrderWithAuthor {
        val oldOrder = orderRepository.getOrder(updatedOrder.id) ?: throw NotFoundException("Order not found")
        if (oldOrder.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Order not found")
        if (oldOrder.status == OrderStatus.CANCELLED) throw BadRequestException("Order canceled")

        val order = orderRepository.updateOrder(updatedOrder)
        val user = userRepository.getUser(order.ownerId) ?: throw NotFoundException("User not found")
        rabbitMQService.publishOrderCreated(routing = application, updatedOrder)
        return OrderWithAuthor(order, user)
    }

    suspend fun closeOrder(userId: String, orderId: String) {
        val order = orderRepository.getOrder(orderId) ?: throw NotFoundException("Order not found")
        if (order.ownerId != userId) throw HttpException(HttpStatusCode.Forbidden, "Order not found")

        val updatedOrder = order.copy(status = OrderStatus.CANCELLED)
        orderRepository.updateOrder(updatedOrder)
        rabbitMQService.publishOrderCreated(routing = application, updatedOrder)
    }

    suspend fun getOrder(id: String): OrderWithAuthor {
        val order = orderRepository.getOrder(id) ?: throw NotFoundException("Order not found")
        val user = userRepository.getUser(order.ownerId) ?: throw NotFoundException("User not found")
        return OrderWithAuthor(order, user)
    }

    suspend fun getOrders(offset: Long, limit: Int): List<OrderWithAuthor> {
        val orders = orderRepository.getLastOrders(offset = offset, limit = limit)
        val users = userRepository.getUsers(orders.map { it.ownerId }.toSet().toList()).associateBy { it.id }
        return orders.map { OrderWithAuthor(order = it, author = users[it.ownerId]!!) }
    }
}