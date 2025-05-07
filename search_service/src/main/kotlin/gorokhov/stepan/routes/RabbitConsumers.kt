package gorokhov.stepan.routes

import gorokhov.stepan.domain.services.OrderSearchService
import gorokhov.stepan.routes.models.OrderRabbitDto
import gorokhov.stepan.routes.models.toModel
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers

class RabbitConsumers(
    private val orderSearchService: OrderSearchService
) {
    fun registerConsumers(routing: Routing) = with(routing) {
        rabbitmq {
            bindQueues()

            connection(id = "consume") {
                consumeCreates()
                consumeUpdates()
            }
        }
    }

    private suspend fun ChannelContext.consumeCreates() {
        basicConsume {
            autoAck = false
            queue = ORDER_CREATE_QUEUE
            dispatcher = Dispatchers.IO
            coroutinePollSize = 1_000
            deliverCallback<OrderRabbitDto> { tag, order ->
                orderSearchService.createOrder(order.toModel())
                basicAck {
                    deliveryTag = tag
                }
            }
        }
    }

    private suspend fun ChannelContext.consumeUpdates() {
        basicConsume {
            autoAck = false
            queue = ORDER_UPDATE_QUEUE
            dispatcher = Dispatchers.IO
            coroutinePollSize = 1_000
            deliverCallback<OrderRabbitDto> { tag, order ->
                orderSearchService.updateOrder(order.toModel())
                basicAck {
                    deliveryTag = tag
                }
            }
        }
    }

    private suspend fun ChannelContext.bindQueues() {
        queueBind {
            queue = ORDER_CREATE_QUEUE
            exchange = ORDER_EXCHANGE
            routingKey = ORDER_CREATED_ROUTING_KEY

            queueDeclare {
                queue = ORDER_CREATE_QUEUE
                autoDelete = false
                durable = true
            }

            declareExchange()
        }

        queueBind {
            queue = ORDER_UPDATE_QUEUE
            exchange = ORDER_EXCHANGE
            routingKey = ORDER_UPDATED_ROUTING_KEY

            queueDeclare {
                queue = ORDER_UPDATE_QUEUE
                autoDelete = false
                durable = true
            }

            declareExchange()
        }

        queueBind {
            queue = ORDER_UPDATE_QUEUE
            exchange = ORDER_EXCHANGE
            routingKey = ORDER_UPDATED_ROUTING_KEY

            queueDeclare {
                queue = ORDER_UPDATE_QUEUE
                autoDelete = false
                durable = true
            }

            declareExchange()
        }
    }

    private suspend fun ChannelContext.declareExchange() {
        exchangeDeclare {
            exchange = ORDER_EXCHANGE
            durable = true
            type = "direct"
        }
    }

    companion object {
        private const val ORDER_EXCHANGE = "orders"

        private const val ORDER_CREATE_QUEUE = "order_create_queue"
        private const val ORDER_CREATED_ROUTING_KEY = "order.created"

        private const val ORDER_UPDATE_QUEUE = "order_update_queue"
        private const val ORDER_UPDATED_ROUTING_KEY = "order.updated"

        private const val ORDER_CANCELLED_QUEUE = "order_create_queue"
        private const val ORDER_CANCELLED_ROUTING_KEY = "order.cancelled"
    }
}