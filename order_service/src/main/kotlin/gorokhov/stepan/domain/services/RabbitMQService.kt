package gorokhov.stepan.domain.services

import gorokhov.stepan.domain.models.Order
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicPublish
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.exchangeDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.rabbitmq
import io.ktor.server.application.*
import io.ktor.server.routing.*

class RabbitMQService {
    fun init(routing: Application) = with(routing) {
        rabbitmq {
            exchangeDeclare {
                exchange = ORDER_EXCHANGE
                durable = true
                type = "direct"
            }
        }
    }

    suspend fun publishOrderCreated(routing: Application, order: Order) = with(routing) {
        rabbitmq {
            basicPublish {
                exchange = ORDER_EXCHANGE
                routingKey = ORDER_CREATED_ROUTING_KEY
                message { order.toRabbitDto() }
            }
        }
    }

    suspend fun publishOrderUpdated(routing: Application, order: Order) = with(routing) {
        rabbitmq {
            basicPublish {
                exchange = ORDER_EXCHANGE
                routingKey = ORDER_UPDATED_ROUTING_KEY
                message { order.toRabbitDto() }
            }
        }
    }

    suspend fun publishOrderCancel(routing: Application, orderId: String) = with(routing) {
        rabbitmq {
            basicPublish {
                exchange = ORDER_EXCHANGE
                routingKey = ORDER_CANCELLED_ROUTING_KEY
                message { mapOf("orderId" to orderId) }
            }
        }
    }

    companion object {
        private const val ORDER_EXCHANGE = "orders"

        private const val ORDER_CREATED_ROUTING_KEY = "order.created"
        private const val ORDER_UPDATED_ROUTING_KEY = "order.updated"
        private const val ORDER_CANCELLED_ROUTING_KEY = "order.cancelled"
    }
} 