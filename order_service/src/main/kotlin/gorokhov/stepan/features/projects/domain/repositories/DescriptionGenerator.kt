package gorokhov.stepan.features.projects.domain.repositories

import gorokhov.stepan.features.projects.domain.models.GeneratedDescription

interface DescriptionGenerator {
    suspend fun generateDescription(prompt: String): GeneratedDescription?
}