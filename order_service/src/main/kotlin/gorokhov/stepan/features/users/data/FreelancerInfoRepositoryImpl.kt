package gorokhov.stepan.features.users.data

import gorokhov.stepan.features.users.data.mappers.toFreelancerInfo
import gorokhov.stepan.features.users.data.tables.FreelancerInfos
import gorokhov.stepan.features.users.domain.FreelancerInfoRepository
import gorokhov.stepan.features.users.domain.models.FreelancerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.upsert

class FreelancerInfoRepositoryImpl : FreelancerInfoRepository {
    override suspend fun getFreelancerInfo(id: String): FreelancerInfo? = dbQuery {
        FreelancerInfos
            .selectAll()
            .where { FreelancerInfos.freelancerId eq id }
            .map { it.toFreelancerInfo() }
            .singleOrNull()
    }

    override suspend fun createFreelancerInfo(freelancerInfo: FreelancerInfo): FreelancerInfo = dbQuery {
        FreelancerInfos.insert {
            it[freelancerId] = freelancerInfo.freelancerId
            it[description] = freelancerInfo.description
            it[skills] = Json.encodeToString(freelancerInfo.skills)
        }
        freelancerInfo
    }

    override suspend fun updateFreelancerInfo(freelancerInfo: FreelancerInfo): FreelancerInfo = dbQuery {
        FreelancerInfos.upsert {
            it[freelancerId] = freelancerInfo.freelancerId
            it[description] = freelancerInfo.description
            it[skills] = Json.encodeToString(freelancerInfo.skills)
        }
        freelancerInfo
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}