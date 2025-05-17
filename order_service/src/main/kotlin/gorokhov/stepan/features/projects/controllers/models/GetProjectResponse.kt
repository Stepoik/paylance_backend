package gorokhov.stepan.features.projects.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class GetProjectsResponse(
    val projects: List<ProjectDto>
)
