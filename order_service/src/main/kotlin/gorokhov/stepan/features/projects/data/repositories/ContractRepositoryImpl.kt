package gorokhov.stepan.features.projects.data.repositories

import gorokhov.stepan.features.projects.data.database.Contracts
import gorokhov.stepan.features.projects.data.mappers.toContract
import gorokhov.stepan.features.projects.domain.models.Contract
import gorokhov.stepan.features.projects.domain.repositories.ContractRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class ContractRepositoryImpl : ContractRepository {

    override suspend fun createContract(contract: Contract): Contract = dbQuery {
        Contracts.insert {
            it[id] = contract.id
            it[projectId] = contract.projectId
            it[freelanceId] = contract.freelanceId
            it[clientId] = contract.clientId
            it[status] = contract.status
        }
        contract
    }

    override suspend fun getFreelancerContracts(freelanceId: String): List<Contract> = dbQuery {
        Contracts
            .selectAll()
            .where { Contracts.freelanceId eq freelanceId }
            .map { it.toContract() }
    }

    override suspend fun getClientContracts(clientId: String): List<Contract> = dbQuery {
        Contracts
            .selectAll()
            .where { Contracts.clientId eq clientId }
            .map { it.toContract() }
    }

    override suspend fun updateContract(contract: Contract): Contract = dbQuery {
        Contracts.update({ Contracts.id eq contract.id }) {
            it[projectId] = contract.projectId
            it[freelanceId] = contract.freelanceId
            it[clientId] = contract.clientId
            it[status] = contract.status
        }
        contract
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}