package gorokhov.stepan.features.projects.controllers.models

import gorokhov.stepan.features.projects.domain.models.ProjectResponse
import gorokhov.stepan.features.projects.domain.models.ResponseStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    val id: String,
    val projectId: String,
    val ownerId: String,
    val freelancerId: String,
    val status: ResponseStatusDto
)

@Serializable
enum class ResponseStatusDto {
    @SerialName("accepted")
    ACCEPTED,

    @SerialName("rejected")
    REJECTED,

    @SerialName("wait_for_accept")
    WAIT_FOR_ACCEPT
}

fun ProjectResponse.toDto(): ResponseDto {
    return ResponseDto(
        id = id,
        projectId = projectId,
        ownerId = ownerId,
        freelancerId = freelanceId,
        status = status.toDto()
    )
}

fun ResponseStatus.toDto(): ResponseStatusDto {
    return when (this) {
        ResponseStatus.ACCEPTED -> ResponseStatusDto.ACCEPTED
        ResponseStatus.REJECTED -> ResponseStatusDto.REJECTED
        ResponseStatus.WAIT_FOR_ACCEPT -> ResponseStatusDto.WAIT_FOR_ACCEPT
    }
}
