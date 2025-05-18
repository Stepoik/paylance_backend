package gorokhov.stepan.features.projects.controllers.models

import gorokhov.stepan.features.projects.domain.models.GeneratedDescription
import kotlinx.serialization.Serializable

@Serializable
data class GenerateDescriptionResponse(
    val title: String,
    val description: String
)

fun GeneratedDescription.toResponse(): GenerateDescriptionResponse {
    return GenerateDescriptionResponse(
        title = title,
        description = description
    )
}
