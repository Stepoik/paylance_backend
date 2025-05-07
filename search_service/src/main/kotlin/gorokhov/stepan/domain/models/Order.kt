package gorokhov.stepan.domain.models

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