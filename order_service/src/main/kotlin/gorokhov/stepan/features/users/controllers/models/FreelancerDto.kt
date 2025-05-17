package gorokhov.stepan.features.users.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class FreelancerDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val skills: List<String>,
    val rating: Double,
    val reviews: List<ReviewDto>,
)

