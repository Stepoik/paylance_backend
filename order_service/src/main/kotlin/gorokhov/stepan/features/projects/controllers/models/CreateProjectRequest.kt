package gorokhov.stepan.features.projects.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.features.projects.domain.models.Project
import gorokhov.stepan.features.projects.domain.models.ProjectStatus
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CreateProjectRequest(
    val title: String,
    val description: String,
    val budget: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime
)

fun CreateProjectRequest.toModel(userId: String): Project {
    return Project(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description,
        deadline = deadline,
        status = ProjectStatus.OPEN,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        budgetRubles = budget,
        ownerId = userId
    )
}