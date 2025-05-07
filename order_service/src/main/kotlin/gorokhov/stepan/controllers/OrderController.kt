package gorokhov.stepan.controllers

import gorokhov.stepan.configurations.AuthenticatedUser
import gorokhov.stepan.controllers.models.*
import gorokhov.stepan.domain.models.OrderStatus
import gorokhov.stepan.data.repositories.OrderRepositoryImpl
import gorokhov.stepan.domain.services.OrderService
import gorokhov.stepan.domain.services.RabbitMQService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrderController : KoinComponent {
    private val orderService: OrderService by inject()

    fun registerRoutes(routing: Routing) = with(routing) {
        route("/orders") {
            authenticate {
                post {
                    val user = call.principal<AuthenticatedUser>()!!
                    val order = call.receive<CreateOrderRequest>()
                    val createdOrder = orderService.createOrder(order = order.toModel(user.id))
                    call.respond(HttpStatusCode.Created, createdOrder.toDto())
                }

                put("/{id}") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val orderId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val request = call.receive<UpdateOrderRequest>()
                    val updatedOrder = request.toModel(orderId)
                    orderService.updateOrder(userId = user.id, updatedOrder = updatedOrder)
                }

                put("/{id}/close") {
                    val user = call.principal<AuthenticatedUser>()!!
                    val orderId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    orderService.closeOrder(userId = user.id, orderId = orderId)
                }
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val order = orderService.getOrder(id).toDto()
                call.respond(order)
            }

            get {
                val offset = call.parameters["offset"]?.toLongOrNull() ?: 0L
                val limit = call.parameters["limit"]?.toInt() ?: DEFAULT_LIMIT
                val orders = orderService.getOrders(offset = offset, limit = limit)
                call.respond(GetOrdersResponse(orders.map { it.toDto() }))
            }
        }
    }

    companion object {
        private const val DEFAULT_LIMIT = 50
    }
}