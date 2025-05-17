package gorokhov.stepan.features.projects.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponseRequest(
    val projectId: String
)
