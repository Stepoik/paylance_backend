package gorokhov.stepan.configurations

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.json.JsonMapper
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient

fun createElasticClient(): ElasticsearchClient {
    val restClient = RestClient.builder(
        HttpHost("elasticsearch", 9200, "http")
    ).build()
    val mapper = JsonMapper.builder()
        .build()


    val transport = RestClientTransport(restClient, JacksonJsonpMapper(mapper))
    return ElasticsearchClient(transport)
}