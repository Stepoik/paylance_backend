package gorokhov.stepan.data

import gorokhov.stepan.data.models.ElasticOrder
import gorokhov.stepan.domain.models.Order

fun Order.toElastic(): ElasticOrder {
    return ElasticOrder(
        id = this.id,
        ownerId = this.ownerId,
        title = this.title,
        description = this.description,
        budget = this.budget,
        deadline = this.deadline,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}