package gorokhov.stepan.configurations

import gorokhov.stepan.features.projects.domain.exceptions.HttpException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<HttpException> { call, cause ->
            call.respond(cause.status, mapOf("message" to cause.message))
        }
    }
}