package gorokhov.stepan.features.projects.domain.services

import gorokhov.stepan.features.projects.domain.models.*
import gorokhov.stepan.features.projects.domain.repositories.ContractRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectResponseRepository
import gorokhov.stepan.features.users.domain.UserRepository
import io.ktor.server.plugins.*
import java.util.*

class ResponseService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val responseRepository: ProjectResponseRepository,
    private val contractRepository: ContractRepository
) {
    suspend fun responseOnProject(freelancerId: String, projectId: String): ProjectResponse {
        val project = projectRepository.getProject(projectId) ?: throw NotFoundException("Project not found")
        val freelancer = userRepository.getUser(freelancerId) ?: throw NotFoundException("User not found")
        val foundResponse = responseRepository.getResponseByProjectAndFreelancerId(projectId = projectId, freelancerId = freelancer.id)
        if (foundResponse != null) {
            return foundResponse
        }
        val response = ProjectResponse(
            id = UUID.randomUUID().toString(),
            projectId = project.id,
            freelanceId = freelancer.id,
            ownerId = project.ownerId,
            status = ResponseStatus.WAIT_FOR_ACCEPT
        )
        return responseRepository.createResponse(response)
    }

    suspend fun replyToResponse(responseId: String, ownerId: String, replyType: ReplyType) {
        val response = responseRepository.getResponse(responseId) ?: throw NotFoundException("Response not found")
        if (ownerId != response.ownerId) throw NotFoundException("Response not found")
        if (replyType == ReplyType.ACCEPT) {
            responseRepository.updateResponse(response.copy(status = ResponseStatus.ACCEPTED))
            contractRepository.createContract(
                Contract(
                    id = UUID.randomUUID().toString(),
                    projectId = response.projectId,
                    freelanceId = response.freelanceId,
                    clientId = response.ownerId,
                    status = ContractStatus.ACTIVE
                )
            )
        } else {
            responseRepository.updateResponse(response.copy(status = ResponseStatus.REJECTED))
        }
    }

    suspend fun getFreelancerResponses(userId: String, offset: Long): List<ProjectResponse> {
        return responseRepository.getResponsesByFreelancerId(userId, offset)
    }

    suspend fun getOwnerResponses(userId: String, offset: Long): List<ProjectResponse> {
        return responseRepository.getResponsesByOwnerId(userId, offset)
    }
}