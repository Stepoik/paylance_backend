package gorokhov.stepan.configurations

import gorokhov.stepan.domain.services.RabbitMQService
import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.exchangeDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.rabbitmq
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRabbitMQ() {
    install(RabbitMQ) {
        uri = "amqp://guest:guest@rabbitmq:5672"
        defaultConnectionName = "default-connection"
        dispatcherThreadPollSize = 4
        tlsEnabled = false
    }
    val rabbitMqService: RabbitMQService by inject()
    routing {
        rabbitMqService.init(this@configureRabbitMQ)
    }
}
