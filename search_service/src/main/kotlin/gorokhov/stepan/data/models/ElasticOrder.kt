package gorokhov.stepan.data.models

import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.models.OrderStatus
import java.time.LocalDateTime

data class ElasticOrder(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val budget: Double,
    val deadline: LocalDateTime,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        0.0,
        LocalDateTime.now(),
        OrderStatus.OPEN,
        LocalDateTime.now(),
        LocalDateTime.now()
    )
}

fun ElasticOrder.toModel(): Order {
    return Order(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        budget = budget,
        deadline = deadline,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}