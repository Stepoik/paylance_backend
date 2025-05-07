package gorokhov.stepan.domain.services

import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.services.models.OrderRabbitDto

fun Order.toRabbitDto(): OrderRabbitDto {
    return OrderRabbitDto(
        id = this.id,
        ownerId = this.ownerId,
        title = this.title,
        description = this.description,
        budget = this.budget,
        deadline = this.deadline,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}