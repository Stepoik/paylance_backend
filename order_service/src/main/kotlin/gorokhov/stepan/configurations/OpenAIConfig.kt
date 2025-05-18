package gorokhov.stepan.configurations

import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig

fun createOpenAIClient(): OpenAI {
    val token = System.getenv("DEEPSEEK_API_TOKEN")
    val config = OpenAIConfig(token)
    return OpenAI(config = config)
}