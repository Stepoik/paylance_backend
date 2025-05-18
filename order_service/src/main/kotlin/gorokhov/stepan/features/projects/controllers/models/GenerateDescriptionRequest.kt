package gorokhov.stepan.features.projects.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class GenerateDescriptionRequest(
    val prompt: String
)
