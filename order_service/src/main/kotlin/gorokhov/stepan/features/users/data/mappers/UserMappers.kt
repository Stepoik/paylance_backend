package gorokhov.stepan.features.users.data.mappers

import com.google.firebase.auth.UserRecord
import gorokhov.stepan.features.users.domain.models.User

fun UserRecord.toDomain(): User {
    return User(
        id = uid,
        name = displayName ?: "",
        imageURL = photoUrl ?: ""
    )
}