package gorokhov.stepan.features.users.domain.models

data class Freelancer(
    val user: User,
    val info: FreelancerInfo,
    val reviews: List<Review>
)

data class FreelancerInfo(
    val freelancerId: String,
    val description: String,
    val skills: List<String>
)
