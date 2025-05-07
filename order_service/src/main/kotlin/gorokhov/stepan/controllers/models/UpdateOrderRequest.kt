package gorokhov.stepan.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UpdateOrderRequest(
    val title: String,
    val description: String,
    val budget: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime
)

fun UpdateOrderRequest.toModel(orderId: String): Order {
    return Order(
        id = orderId,
        title = title,
        description = description,
        deadline = deadline,
        status = OrderStatus.OPEN,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        budget = budget,
        ownerId = ""
    )
}
