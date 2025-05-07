package gorokhov.stepan.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UidIdentifier
import gorokhov.stepan.domain.models.User
import gorokhov.stepan.domain.repositories.UserRepository

class FirebaseUserRepository(
    private val auth: FirebaseAuth
) : UserRepository {
    override suspend fun getUsers(ids: List<String>): List<User> {
        val userIdentifiers = ids.map { UidIdentifier(it) }
        return auth.getUsers(userIdentifiers).users.map { it.toDomain() }
    }

    override suspend fun getUser(id: String): User? {
        return auth.getUser(id)?.toDomain()
    }
}