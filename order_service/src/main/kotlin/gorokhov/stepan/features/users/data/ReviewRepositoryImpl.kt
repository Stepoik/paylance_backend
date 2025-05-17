package gorokhov.stepan.features.users.data

import gorokhov.stepan.features.users.data.mappers.toReview
import gorokhov.stepan.features.users.data.tables.Reviews
import gorokhov.stepan.features.users.domain.ReviewRepository
import gorokhov.stepan.features.users.domain.models.Review
import gorokhov.stepan.features.users.domain.models.UserType
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ReviewRepositoryImpl : ReviewRepository {
    override suspend fun getReviews(userId: String, userType: UserType): List<Review> = dbQuery {
        Reviews
            .selectAll()
            .where { (Reviews.userId eq userId) and (Reviews.userType eq userType)  }
            .map { it.toReview() }
    }

    override suspend fun createReview(review: Review) = dbQuery {
        Reviews.insert {
            it[userId] = review.userId
            it[title] = review.title
            it[text] = review.text
            it[rate] = review.rate
            it[userType] = review.userType
        }
        review
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}