package gorokhov.stepan.features.users.domain

import gorokhov.stepan.features.users.domain.models.Review
import gorokhov.stepan.features.users.domain.models.UserType

interface ReviewRepository {
    suspend fun getReviews(userId: String, userType: UserType): List<Review>

    suspend fun createReview(review: Review): Review
}