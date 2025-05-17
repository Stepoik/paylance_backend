package gorokhov.stepan.features.users.data.mappers

import gorokhov.stepan.features.users.data.tables.Reviews
import gorokhov.stepan.features.users.domain.models.Review
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toReview(): Review =
    Review(
        id = this[Reviews.id],
        userId = this[Reviews.userId],
        title = this[Reviews.title],
        text = this[Reviews.text],
        rate = this[Reviews.rate],
        userType = this[Reviews.userType]
    )