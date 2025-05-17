package gorokhov.stepan.features.projects.domain.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class Project(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val budgetRubles: Double,
    val deadline: LocalDateTime,
    val status: ProjectStatus = ProjectStatus.OPEN,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

@Serializable
enum class ProjectStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}