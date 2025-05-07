package gorokhov.stepan.controllers.models

import gorokhov.stepan.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val id: String,
    val name: String,
    val imageURL: String
)

fun User.toAuthor() = AuthorDto(
    id = id,
    name = name,
    imageURL = imageURL
)
