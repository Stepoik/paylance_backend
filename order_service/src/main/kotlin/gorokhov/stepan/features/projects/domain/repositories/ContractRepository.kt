package gorokhov.stepan.features.projects.domain.repositories

import gorokhov.stepan.features.projects.domain.models.Contract

interface ContractRepository {
    suspend fun createContract(contract: Contract): Contract

    suspend fun getFreelancerContracts(freelanceId: String): List<Contract>

    suspend fun getClientContracts(clientId: String): List<Contract>

    suspend fun updateContract(contract: Contract): Contract

    suspend fun getContractByProjectIdAndUserId(userId: String, projectId: String): Contract?
}