package gorokhov.stepan.features.notifications

import gorokhov.stepan.features.notifications.domain.Notification
import gorokhov.stepan.features.notifications.domain.NotificationType
import gorokhov.stepan.features.notifications.domain.repositories.NotificationRepository
import gorokhov.stepan.features.notifications.domain.services.NotificationService
import gorokhov.stepan.features.projects.domain.models.ReplyType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.*

class NotificationServiceFuzzingTest {

    private lateinit var repository: NotificationRepository
    private lateinit var notificationService: NotificationService

    @BeforeEach
    fun setup() {
        repository = mock()
        notificationService = NotificationService(repository)
    }

    @Test
    fun `createProjectResponseNotification should handle random input data`(): Unit = runBlocking {
        val arbProjectOwnerId = Arb.uuid().map { it.toString() }
        val arbProjectId = Arb.uuid().map { it.toString() }
        val arbResponseId = Arb.uuid().map { it.toString() }
        val arbFreelancerName = Arb.string(1..50)

        checkAll(arbProjectOwnerId, arbProjectId, arbResponseId, arbFreelancerName) { projectOwnerId, projectId, responseId, freelancerName ->
            // Подготовка
            whenever(repository.save(any())).thenReturn(Unit)

            // Действие
            notificationService.createProjectResponseNotification(
                projectOwnerId = projectOwnerId,
                projectId = projectId,
                responseId = responseId,
                freelancerName = freelancerName
            )

            // Проверка
            verify(repository).save(argThat { savedNotification ->
                savedNotification.userId == projectOwnerId &&
                savedNotification.type == NotificationType.PROJECT_RESPONSE &&
                savedNotification.projectId == projectId &&
                savedNotification.responseId == responseId &&
                savedNotification.message.contains(freelancerName) &&
                !savedNotification.isRead &&
                savedNotification.title == "Новый отклик на проект"
            })
        }
    }

    @Test
    fun `createResponseResultNotification should handle random input data`(): Unit = runBlocking {
        val arbFreelancerId = Arb.uuid().map { it.toString() }
        val arbProjectId = Arb.uuid().map { it.toString() }
        val arbProjectTitle = Arb.string(1..100)
        val arbReplyType = Arb.enum<ReplyType>()

        checkAll(arbFreelancerId, arbProjectId, arbProjectTitle, arbReplyType) { freelancerId, projectId, projectTitle, replyType ->
            // Подготовка
            val (expectedTitle, expectedMessage) = when (replyType) {
                ReplyType.ACCEPT -> "Ваш отклик принят" to "Ваш отклик на проект \"$projectTitle\" был принят"
                ReplyType.REJECT -> "Ваш отклик отклонен" to "Ваш отклик на проект \"$projectTitle\" был отклонен"
            }

            whenever(repository.save(any())).thenReturn(Unit)

            // Действие
            notificationService.createResponseResultNotification(
                freelancerId = freelancerId,
                projectId = projectId,
                replyType = replyType,
                projectTitle = projectTitle
            )

            // Проверка
            verify(repository).save(argThat { savedNotification ->
                savedNotification.userId == freelancerId &&
                savedNotification.type == NotificationType.RESPONSE_RESULT &&
                savedNotification.projectId == projectId &&
                savedNotification.title == expectedTitle &&
                savedNotification.message == expectedMessage &&
                !savedNotification.isRead &&
                savedNotification.responseId.isEmpty()
            })
        }
    }

    @Test
    fun `createProjectResponseNotification should handle empty freelancer name`() = runBlocking {
        val projectOwnerId = UUID.randomUUID().toString()
        val projectId = UUID.randomUUID().toString()
        val responseId = UUID.randomUUID().toString()
        val freelancerName = ""

        whenever(repository.save(any())).thenReturn(Unit)

        notificationService.createProjectResponseNotification(
            projectOwnerId = projectOwnerId,
            projectId = projectId,
            responseId = responseId,
            freelancerName = freelancerName
        )

        verify(repository).save(argThat { savedNotification ->
            savedNotification.message == "Пользователь  откликнулся на ваш проект"
        })
    }

    @Test
    fun `createResponseResultNotification should handle empty project title`() = runBlocking {
        val freelancerId = UUID.randomUUID().toString()
        val projectId = UUID.randomUUID().toString()
        val projectTitle = ""
        val replyType = ReplyType.ACCEPT

        whenever(repository.save(any())).thenReturn(Unit)

        notificationService.createResponseResultNotification(
            freelancerId = freelancerId,
            projectId = projectId,
            replyType = replyType,
            projectTitle = projectTitle
        )

        verify(repository).save(argThat { savedNotification ->
            savedNotification.message == "Ваш отклик на проект \"\" был принят"
        })
    }
} 