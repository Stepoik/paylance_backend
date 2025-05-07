package gorokhov.stepan.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CreateOrderRequest(
    val title: String,
    val description: String,
    val budget: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime
)

fun CreateOrderRequest.toModel(userId: String): Order {
    return Order(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description,
        deadline = deadline,
        status = OrderStatus.OPEN,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        budget = budget,
        ownerId = userId
    )
}