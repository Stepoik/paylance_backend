package gorokhov.stepan.features.users.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class FreelancerDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val reviews: List<ReviewDto>,

)

