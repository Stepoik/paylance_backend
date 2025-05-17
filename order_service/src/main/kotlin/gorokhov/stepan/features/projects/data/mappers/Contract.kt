package gorokhov.stepan.features.projects.data.mappers

import gorokhov.stepan.features.projects.data.database.Contracts
import gorokhov.stepan.features.projects.domain.models.Contract
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toContract(): Contract =
    Contract(
        id = this[Contracts.id],
        projectId = this[Contracts.projectId],
        freelanceId = this[Contracts.freelanceId],
        clientId = this[Contracts.clientId],
        status = this[Contracts.status]
    )