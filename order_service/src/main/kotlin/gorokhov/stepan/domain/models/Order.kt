package gorokhov.stepan.domain.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class Order(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val budget: Double,
    val deadline: LocalDateTime,
    val status: OrderStatus = OrderStatus.OPEN,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Serializable
enum class OrderStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Serializable
data class OrderResponse(
    val id: String,
    val orderId: String,
    val freelancerId: String,
    val proposal: String,
    val price: Double,
    val status: ResponseStatus = ResponseStatus.PENDING,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Serializable
enum class ResponseStatus {
    PENDING,
    ACCEPTED,
    REJECTED
} 