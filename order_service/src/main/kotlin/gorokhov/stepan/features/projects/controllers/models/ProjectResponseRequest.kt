package gorokhov.stepan.features.projects.controllers.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponseRequest(
    @SerialName("project_id")
    val projectId: String
)
