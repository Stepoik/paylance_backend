package gorokhov.stepan.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class GetOrdersResponse(
    val orders: List<OrderDto>
)
