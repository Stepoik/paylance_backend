package gorokhov.stepan.features.projects.domain.models

data class Contract(
    val id: String,
    val projectId: String,
    val freelanceId: String,
    val clientId: String,
    val status: ContractStatus
)

enum class ContractStatus {
    ACTIVE,
    CLOSED,
    COMPLETED
}
