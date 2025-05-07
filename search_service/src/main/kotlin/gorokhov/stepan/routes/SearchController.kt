package gorokhov.stepan.routes

import gorokhov.stepan.domain.services.OrderSearchService
import gorokhov.stepan.routes.models.toDto
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class SearchController(
    private val orderSearchService: OrderSearchService
) {
    fun registerRoutes(routing: Routing) = with(routing) {
        route("/search") {
            get("/orders") {
                val text = call.queryParameters["text"] ?: return@get call.respond(HttpStatusCode.BadRequest)

                val orders = orderSearchService.searchOrder(text)
                call.respond(orders.map { it.toDto() })
            }
        }
    }
} 