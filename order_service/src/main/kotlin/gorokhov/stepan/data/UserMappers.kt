package gorokhov.stepan.data

import com.google.firebase.auth.UserRecord
import gorokhov.stepan.domain.models.User

fun UserRecord.toDomain(): User {
    return User(
        id = uid,
        name = displayName,
        imageURL = photoUrl
    )
}