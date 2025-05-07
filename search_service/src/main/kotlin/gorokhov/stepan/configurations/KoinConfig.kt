package gorokhov.stepan.configurations

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.google.firebase.auth.FirebaseAuth
import gorokhov.stepan.routes.SearchController
import gorokhov.stepan.data.ElasticOrderSearchRepository
import gorokhov.stepan.data.FirebaseUserRepository
import gorokhov.stepan.domain.repositories.OrderSearchRepository
import gorokhov.stepan.domain.repositories.UserRepository
import gorokhov.stepan.domain.services.OrderSearchService
import gorokhov.stepan.routes.RabbitConsumers
import io.ktor.server.application.*
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun Application.configureKoin() {
    startKoin {
        modules(module {
            single { FirebaseAuth.getInstance() }
            single { createElasticClient() }
            single { SearchController(get()) }
            single { RabbitConsumers(get()) }
            single<OrderSearchRepository> { ElasticOrderSearchRepository(get()) }
            single { OrderSearchService(get(), get()) }
            single<UserRepository> { FirebaseUserRepository(get()) }
        })
    }
}