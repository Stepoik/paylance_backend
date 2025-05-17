package gorokhov.stepan.features.projects.domain.models

import gorokhov.stepan.features.users.domain.models.User

data class ProjectWithAuthor(
    val project: Project,
    val author: User,
    val isRespond: Boolean
)