package gorokhov.stepan.features.users.domain.models

data class Review(
    val id: String,
    val userId: String,
    val title: String,
    val text: String,
    val rate: Int,
    val userType: UserType
)

enum class UserType {
    CLIENT,
    FREELANCER
}
