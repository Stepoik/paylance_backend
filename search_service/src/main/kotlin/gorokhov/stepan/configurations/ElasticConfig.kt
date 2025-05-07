package gorokhov.stepan.configurations

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

fun createElasticClient(): ElasticsearchClient {
    val restClient = RestClient.builder(
        HttpHost("localhost", 9200, "http")
    ).build()
    val mapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .build()


    val transport = RestClientTransport(restClient, JacksonJsonpMapper(mapper))
    return ElasticsearchClient(transport)
}

class LenientLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val text = p.text.trim()

        return try {
            LocalDateTime.parse(text)
        } catch (e: DateTimeParseException) {
            // Если ISO не сработал — обрезаем до наносекунд максимум 6 знаков
            val truncated = text.replace(Regex("(\\.\\d{6})\\d+"), "$1")
            LocalDateTime.parse(truncated)
        }
    }
}