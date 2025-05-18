package gorokhov.stepan.configurations

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.statuspages.*
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> true }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            // Это покажет причину (stacktrace) ошибки 500
            applicationEnvironment().log.error("Unhandled exception: ${cause.message}", cause)

            call.respond(HttpStatusCode.InternalServerError, null)
        }
    }
}