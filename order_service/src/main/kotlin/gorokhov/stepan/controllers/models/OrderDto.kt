package gorokhov.stepan.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import gorokhov.stepan.domain.models.OrderWithAuthor
import gorokhov.stepan.domain.models.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OrderDto(
    val id: String,
    val author: AuthorDto,
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

fun OrderWithAuthor.toDto(): OrderDto {
    return OrderDto(
        id = order.id,
        author = author.toAuthor(),
        title = order.title,
        description = order.description,
        budget = order.budget,
        deadline = order.deadline,
        status = order.status,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
    )
}