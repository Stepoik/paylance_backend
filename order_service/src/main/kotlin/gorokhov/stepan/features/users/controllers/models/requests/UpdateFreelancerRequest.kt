package gorokhov.stepan.features.users.controllers.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFreelancerRequest(
    val description: String,
    val skills: List<String>
)
