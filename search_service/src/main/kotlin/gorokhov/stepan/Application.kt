package gorokhov.stepan

import gorokhov.stepan.configurations.*
import gorokhov.stepan.routes.RabbitConsumers
import gorokhov.stepan.routes.SearchController
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureKoin()
    configureRabbitMQ()
    configureSerialization()

    val searchController: SearchController by inject()
    val rabbitConsumers: RabbitConsumers by inject()
    routing {
        searchController.registerRoutes(this)
        rabbitConsumers.registerConsumers(this)
    }
} 