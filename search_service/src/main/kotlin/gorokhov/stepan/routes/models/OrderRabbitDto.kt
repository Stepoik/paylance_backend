package gorokhov.stepan.routes.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.domain.models.Order
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

fun OrderRabbitDto.toModel(): Order {
    return Order(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        budget = budget,
        deadline = deadline,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
