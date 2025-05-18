package gorokhov.stepan

import com.google.firebase.auth.FirebaseAuth
import gorokhov.stepan.configurations.*
import gorokhov.stepan.features.chats.controllers.ChatController
import gorokhov.stepan.features.chats.data.ChatRepositoryImpl
import gorokhov.stepan.features.chats.domain.repositories.ChatRepository
import gorokhov.stepan.features.chats.domain.services.ChatService
import gorokhov.stepan.features.notifications.controllers.NotificationController
import gorokhov.stepan.features.notifications.data.NotificationRepositoryImpl
import gorokhov.stepan.features.notifications.domain.repositories.NotificationRepository
import gorokhov.stepan.features.notifications.domain.services.NotificationService
import gorokhov.stepan.features.projects.controllers.ProjectController
import gorokhov.stepan.features.projects.controllers.ResponseController
import gorokhov.stepan.features.projects.data.repositories.*
import gorokhov.stepan.features.projects.domain.repositories.ContractRepository
import gorokhov.stepan.features.projects.domain.repositories.DescriptionGenerator
import gorokhov.stepan.features.projects.domain.repositories.ProjectRepository
import gorokhov.stepan.features.projects.domain.repositories.ProjectResponseRepository
import gorokhov.stepan.features.projects.domain.services.ProjectService
import gorokhov.stepan.features.projects.domain.services.ResponseService
import gorokhov.stepan.features.users.controllers.UserController
import gorokhov.stepan.features.users.data.FirebaseUserRepository
import gorokhov.stepan.features.users.data.FreelancerInfoRepositoryImpl
import gorokhov.stepan.features.users.data.ReviewRepositoryImpl
import gorokhov.stepan.features.users.domain.FreelancerInfoRepository
import gorokhov.stepan.features.users.domain.ReviewRepository
import gorokhov.stepan.features.users.domain.UserRepository
import gorokhov.stepan.features.users.domain.services.UserService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val application = this
    startKoin {
        modules(module {
            single { createElasticClient() }
            single { createFirebaseApp() }
            single { createOpenAIClient() }
            single { FirebaseAuth.getInstance(get()) }
            single<Application> { application }

            single { ProjectController() }
            single { ResponseController() }
            single { UserController() }
            single { NotificationController(get()) }
            single { ChatController(get()) }

            single { NotificationService(get()) }
            single { ProjectService(get(), get(), get(), get()) }
            single { UserService(get(), get(), get()) }
            single { ResponseService(get(), get(), get(), get(), get(), get()) }
            single { ChatService(get(), get(), get()) }

            single<ProjectRepository> { ProjectRepositoryImpl() }
            single<UserRepository> { FirebaseUserRepository(get()) }
            single<ProjectResponseRepository> { ProjectResponseRepositoryImpl() }
            single<ContractRepository> { ContractRepositoryImpl() }
            single<FreelancerInfoRepository> { FreelancerInfoRepositoryImpl() }
            single<ReviewRepository> { ReviewRepositoryImpl() }
            single<NotificationRepository> { NotificationRepositoryImpl() }
            single<ChatRepository> { ChatRepositoryImpl() }
            single<DescriptionGenerator> { OpenAIDescriptionGenerator(get()) }
        })
    }
    configureDatabases()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureStatusPages()
    configureWebsocket()
    configureLogging()

    registerRoutes()
} 