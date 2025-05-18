package gorokhov.stepan.configurations

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebsocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds
    }
}