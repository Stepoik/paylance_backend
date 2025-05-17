package gorokhov.stepan.features.projects.domain.models

data class UserResponses(
    val clientResponses: List<ProjectResponse>,
    val freelancerResponses: List<ProjectResponse>,
)
