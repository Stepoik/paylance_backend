package gorokhov.stepan.domain.exceptions

import io.ktor.http.*

class HttpException(
    val status: HttpStatusCode,
    override val message: String
) : RuntimeException(message)