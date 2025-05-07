package gorokhov.stepan.configurations

import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.ktor.server.application.*

fun Application.configureRabbitMQ() {
    install(RabbitMQ) {
        uri = "amqp://guest:guest@rabbitmq:5672"
        defaultConnectionName = "default-connection"
        dispatcherThreadPollSize = 4
        tlsEnabled = false
    }
}
