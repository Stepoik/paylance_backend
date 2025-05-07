package gorokhov.stepan.domain.models

data class OrderWithAuthor(
    val order: Order,
    val author: User
)