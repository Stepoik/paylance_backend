package gorokhov.stepan.features.notifications.controllers.models.responses

import gorokhov.stepan.features.notifications.controllers.models.NotificationDto
import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationsResponse(
    val notifications: List<NotificationDto>,
    val total: Long
)
