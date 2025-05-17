package gorokhov.stepan.features.users.domain

import gorokhov.stepan.features.users.domain.models.User

interface UserRepository {
    suspend fun getUsers(ids: List<String>): List<User>

    suspend fun getUser(id: String): User?
}