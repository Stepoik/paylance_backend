package gorokhov.stepan.routes.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchOrderResponse(
    val orders: List<OrderDto>
)
