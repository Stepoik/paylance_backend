package gorokhov.stepan.configurations

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDateTime

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}