package gorokhov.stepan.features.users.data.mappers

import gorokhov.stepan.features.users.data.tables.FreelancerInfos
import gorokhov.stepan.features.users.domain.models.FreelancerInfo
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toFreelancerInfo(): FreelancerInfo =
    FreelancerInfo(
        freelancerId = this[FreelancerInfos.freelancerId],
        description = this[FreelancerInfos.description]
    )