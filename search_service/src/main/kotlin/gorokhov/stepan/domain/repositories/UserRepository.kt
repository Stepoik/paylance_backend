package gorokhov.stepan.domain.repositories

import gorokhov.stepan.domain.models.User


interface UserRepository {
    suspend fun getUsers(ids: List<String>): List<User>

    suspend fun getUser(id: String): User?
}