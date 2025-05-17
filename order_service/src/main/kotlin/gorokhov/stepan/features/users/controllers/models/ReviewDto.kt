package gorokhov.stepan.features.users.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val title: String,
    val text: String,
    val rate: Int
)
