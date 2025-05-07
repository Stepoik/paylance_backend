package gorokhov.stepan

import gorokhov.stepan.configurations.*
import gorokhov.stepan.controllers.OrderController
import gorokhov.stepan.data.repositories.OrderRepositoryImpl
import gorokhov.stepan.domain.services.OrderService
import gorokhov.stepan.domain.services.RabbitMQService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    startKoin {
        modules(module {
            single<Application> { this@module }
            single { OrderController() }
            single { OrderRepositoryImpl() }
            single { RabbitMQService() }
            single { OrderService(get(), get(), get(), get()) }
        })
    }
    configureDatabases()
    configureSecurity()
    configureHTTP()
    configureRabbitMQ()
    configureSerialization()
    configureStatusPages()

    // Настройка маршрутизации
    val orderController: OrderController by inject()
    routing {
        orderController.registerRoutes(this)
    }
} 