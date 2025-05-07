package gorokhov.stepan.domain.services.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.domain.models.OrderStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OrderRabbitDto(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val budget: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,
    val status: OrderStatus,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
)
