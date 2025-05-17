package gorokhov.stepan.features.users.domain.services

import gorokhov.stepan.features.users.domain.FreelancerInfoRepository
import gorokhov.stepan.features.users.domain.ReviewRepository
import gorokhov.stepan.features.users.domain.UserRepository
import gorokhov.stepan.features.users.domain.models.Freelancer
import gorokhov.stepan.features.users.domain.models.FreelancerInfo
import gorokhov.stepan.features.users.domain.models.UserType
import io.ktor.server.plugins.*

class UserService(
    private val freelancerRepository: FreelancerInfoRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {
    suspend fun getFreelancer(id: String): Freelancer {
        var freelancerInfo = freelancerRepository.getFreelancerInfo(id)
        val reviews = reviewRepository.getReviews(userId = id, userType = UserType.FREELANCER)
        val user = userRepository.getUser(id) ?: throw NotFoundException("User not found")
        if (freelancerInfo != null) {
            return Freelancer(user = user, info = freelancerInfo, reviews = reviews)
        }
        freelancerInfo = emptyFreelanceInfo(id)
        freelancerRepository.createFreelancerInfo(freelancerInfo = freelancerInfo)
        return Freelancer(user = user, info = freelancerInfo, reviews = reviews)
    }

    suspend fun updateFreelancerInfo(freelancerInfo: FreelancerInfo) {
        freelancerRepository.updateFreelancerInfo(freelancerInfo)
    }
}

fun emptyFreelanceInfo(freelancerId: String) = FreelancerInfo(
    freelancerId = freelancerId,
    description = "Пусто",
    skills = listOf()
)