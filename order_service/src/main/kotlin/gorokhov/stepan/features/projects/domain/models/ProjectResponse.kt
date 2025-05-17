package gorokhov.stepan.features.projects.domain.models

data class ProjectResponse(
    val id: String,
    val projectId: String,
    val ownerId: String,
    val freelanceId: String,
    val status: ResponseStatus
)

enum class ResponseStatus {
    ACCEPTED,
    REJECTED,
    WAIT_FOR_ACCEPT
}
