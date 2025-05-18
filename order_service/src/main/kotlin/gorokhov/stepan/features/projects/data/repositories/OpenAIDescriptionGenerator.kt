package gorokhov.stepan.features.projects.data.repositories

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import gorokhov.stepan.features.projects.domain.models.GeneratedDescription
import gorokhov.stepan.features.projects.domain.repositories.DescriptionGenerator
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val GENERATE_DESCRIPTION_PROMPT = """
Мне нужно красиво и конструктивно сделать описание и название задаче, по наброску описания
Формат возращения такой:
{
"title": "string",
"description": "string"
}
"""

class OpenAIDescriptionGenerator(
    private val openAIClient: OpenAI
) : DescriptionGenerator {
    override suspend fun generateDescription(prompt: String): GeneratedDescription? {
        val request = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = GENERATE_DESCRIPTION_PROMPT
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            )
        )
        val content = openAIClient.chatCompletion(request).choices.firstOrNull()?.message?.content ?: return null
        return runCatching {
            Json.decodeFromString<DeepSeekAnswer>(content).toGeneratedDescription()
        }.getOrNull()
    }
}

@Serializable
data class DeepSeekAnswer(
    val title: String,
    val description: String
)

fun DeepSeekAnswer.toGeneratedDescription(): GeneratedDescription {
    return GeneratedDescription(title, description)
}