package gorokhov.stepan.features.users.controllers.mappers

import gorokhov.stepan.features.users.controllers.models.FreelancerDto
import gorokhov.stepan.features.users.controllers.models.ReviewDto
import gorokhov.stepan.features.users.domain.models.Freelancer
import gorokhov.stepan.features.users.domain.models.Review

fun Freelancer.toDto(): FreelancerDto {
    return FreelancerDto(
        id = user.id,
        name = user.name,
        imageUrl = user.imageURL,
        reviews = reviews.map { it.toDto() }
    )
}

fun Review.toDto(): ReviewDto {
    return ReviewDto(
        title = title,
        text = text,
        rate = rate,
    )
}