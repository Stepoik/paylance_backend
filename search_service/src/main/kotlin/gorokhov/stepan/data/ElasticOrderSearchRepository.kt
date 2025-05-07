package gorokhov.stepan.data

import co.elastic.clients.elasticsearch.ElasticsearchClient
import gorokhov.stepan.data.models.ElasticOrder
import gorokhov.stepan.data.models.toModel
import gorokhov.stepan.domain.models.Order
import gorokhov.stepan.domain.repositories.OrderSearchRepository

class ElasticOrderSearchRepository(
    private val elasticsearchClient: ElasticsearchClient
) : OrderSearchRepository {
    override suspend fun indexOrder(order: Order) {
        elasticsearchClient.index { req ->
            req.index("orders")
                .id(order.id)
                .document(order.toElastic())
        }
    }

    override suspend fun searchOrder(text: String): List<Order> {
        try {
            val response = elasticsearchClient.search({ s ->
                s.index("orders")
                    .query { q ->
                        q.multiMatch { mm ->
                            mm.query(text)
                                .fields("title", "description")
                        }
                    }
            }, ElasticOrder::class.java)

            return response.hits().hits().mapNotNull { it.source()?.toModel() }
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }
}