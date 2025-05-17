package gorokhov.stepan.features.projects.data.mappers

import gorokhov.stepan.features.projects.data.database.ProjectResponses
import gorokhov.stepan.features.projects.domain.models.ProjectResponse
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toProjectResponse(): ProjectResponse =
    ProjectResponse(
        id = this[ProjectResponses.id],
        projectId = this[ProjectResponses.projectId],
        ownerId = this[ProjectResponses.ownerId],
        freelanceId = this[ProjectResponses.freelanceId],
        status = this[ProjectResponses.status]
    )