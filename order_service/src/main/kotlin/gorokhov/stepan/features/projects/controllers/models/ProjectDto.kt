package gorokhov.stepan.features.projects.controllers.models

import gorokhov.stepan.configurations.serializers.LocalDateTimeSerializer
import gorokhov.stepan.features.projects.domain.models.ProjectStatus
import gorokhov.stepan.features.projects.domain.models.ProjectWithAuthor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ProjectDto(
    val id: String,
    val author: AuthorDto,
    val title: String,
    val description: String,
    val budget: Double,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,
    val status: ProjectStatus,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    val skills: List<String>,
    val isRespond: Boolean
)

fun ProjectWithAuthor.toDto(): ProjectDto {
    return ProjectDto(
        id = project.id,
        author = author.toAuthor(),
        title = project.title,
        description = project.description,
        budget = project.budgetRubles,
        deadline = project.deadline,
        status = project.status,
        createdAt = project.createdAt,
        updatedAt = project.updatedAt,
        skills = project.skills,
        isRespond = isRespond
    )
}