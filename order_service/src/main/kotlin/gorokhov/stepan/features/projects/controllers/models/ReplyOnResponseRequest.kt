package gorokhov.stepan.features.projects.controllers.models

import gorokhov.stepan.features.projects.domain.models.ReplyType
import kotlinx.serialization.Serializable


@Serializable
data class ReplyOnResponseRequest(
    val responseId: String,
    val replyType: ReplyTypeDto
)

@Serializable
enum class ReplyTypeDto {
    ACCEPTED,
    REJECTED,
}

fun ReplyTypeDto.toDomain(): ReplyType {
    return when (this) {
        ReplyTypeDto.ACCEPTED -> ReplyType.ACCEPT
        ReplyTypeDto.REJECTED -> ReplyType.REJECT
    }
}