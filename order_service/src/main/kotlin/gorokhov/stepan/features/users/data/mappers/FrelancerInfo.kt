package gorokhov.stepan.features.users.data.mappers

import gorokhov.stepan.features.users.data.tables.FreelancerInfos
import gorokhov.stepan.features.users.domain.models.FreelancerInfo
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toFreelancerInfo(): FreelancerInfo {
    val skillsString = this[FreelancerInfos.skills]
    val skillsList = Json.decodeFromString<List<String>>(skillsString)
    return FreelancerInfo(
        freelancerId = this[FreelancerInfos.freelancerId],
        description = this[FreelancerInfos.description],
        skills = skillsList
    )
}