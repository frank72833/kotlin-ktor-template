package com.fsn.template.application.configuration

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ErrorsResponse(
    val errors: List<ErrorResponse>,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)

@Serializable
data class ErrorResponse(
    val message: String,
)

fun Application.configureErrorHandlers() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ErrorsResponse(
                    errors = listOf(ErrorResponse(
                        message = cause.localizedMessage
                    )),
                    timestamp = Instant.now()
                )
            )
        }

        exception<RequestValidationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ErrorsResponse(
                    errors = cause.reasons.map { ErrorResponse(message = it) },
                    timestamp = Instant.now()
                )
            )
        }
    }
}
