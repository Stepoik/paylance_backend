package gorokhov.stepan

import gorokhov.stepan.features.projects.controllers.ProjectController
import gorokhov.stepan.features.projects.controllers.ResponseController
import gorokhov.stepan.features.users.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerRoutes() {
    val projectController: ProjectController by inject()
    val responseController: ResponseController by inject()
    val userController: UserController by inject()
    routing {
        projectController.registerRoutes(this)
        responseController.registerRoutes(this)
        userController.registerRoutes(this)
    }
}