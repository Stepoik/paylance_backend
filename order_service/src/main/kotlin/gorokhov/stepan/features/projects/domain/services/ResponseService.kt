package gorokhov.stepan.features.projects.domain.services

import gorokhov.stepan.features.chats.domain.models.Chat
import gorokhov.stepan.features.chats.domain.models.CreateChat
import gorokhov.stepan.features.chats.domain.repositories.ChatRepository
import gorokhov.stepan.features.notifications.domain.services.NotificationService
import gorokhov.stepan.features.projects.domain.exceptions.HttpException
import gorokhov.stepan.features.projects.domain.models.*
import gorokhov.stepan.features.projects.domain.repositories.ContractRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectResponseRepository
import gorokhov.stepan.features.users.domain.UserRepository
import io.ktor.http.*
import io.ktor.server.plugins.*
import java.util.*

class ResponseService(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val responseRepository: ProjectResponseRepository,
    private val contractRepository: ContractRepository,
    private val chatRepository: ChatRepository,
    private val notificationService: NotificationService
) {
    suspend fun responseOnProject(freelancerId: String, projectId: String): ProjectResponse {
        val project = projectRepository.getProject(projectId) ?: throw NotFoundException("Project not found")
        val freelancer = userRepository.getUser(freelancerId) ?: throw NotFoundException("User not found")
        val foundResponse =
            responseRepository.getResponseByProjectAndFreelancerId(projectId = projectId, freelancerId = freelancer.id)
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
        val createdResponse = responseRepository.createResponse(response)

        // Создаем уведомление для владельца проекта
        notificationService.createProjectResponseNotification(
            projectOwnerId = project.ownerId,
            projectId = project.id,
            responseId = response.id,
            freelancerName = freelancer.name
        )

        return createdResponse
    }

    suspend fun replyToResponse(responseId: String, ownerId: String, replyType: ReplyType) {
        val response = responseRepository.getResponse(responseId) ?: throw NotFoundException("Response not found")
        if (ownerId != response.ownerId) throw NotFoundException("Response not found")

        val project = projectRepository.getProject(response.projectId) ?: throw NotFoundException("Project not found")
        if (response.status != ResponseStatus.WAIT_FOR_ACCEPT) {
            throw HttpException(HttpStatusCode.Conflict, message = "Response already accepted")
        }

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
            chatRepository.createChat(
                CreateChat(
                    id = UUID.randomUUID().toString(),
                    projectId = response.projectId,
                    freelancerId = response.freelanceId,
                    clientId = response.ownerId,
                    title = project.title
                )
            )
        } else {
            responseRepository.updateResponse(response.copy(status = ResponseStatus.REJECTED))
        }

        // Создаем уведомление для фрилансера о результате отклика
        notificationService.createResponseResultNotification(
            freelancerId = response.freelanceId,
            projectId = response.projectId,
            replyType = replyType,
            projectTitle = project.title
        )
    }

    suspend fun getResponseById(responseId: String): ProjectResponse {
        return responseRepository.getResponse(responseId) ?: throw NotFoundException("Response not found")
    }

    suspend fun getFreelancerResponses(userId: String, offset: Long): List<ProjectResponse> {
        return responseRepository.getResponsesByFreelancerId(userId, offset)
    }

    suspend fun getOwnerResponses(userId: String, offset: Long): List<ProjectResponse> {
        return responseRepository.getResponsesByOwnerId(userId, offset)
    }
}