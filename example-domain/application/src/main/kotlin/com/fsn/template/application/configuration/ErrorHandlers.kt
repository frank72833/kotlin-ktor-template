package com.fsn.template.application.configuration

import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.core.getLogger
import com.fsn.template.infrastructure.configuration.OptimisticRepositoryError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond

private var LOG = getLogger<Application>()

fun Application.configureErrorHandlers() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            LOG.error("An error has occurred", cause)

            call.respond(
                status = HttpStatusCode.InternalServerError,
                message =
                ErrorHttpResponse(
                    statusCode = HttpStatusCode.InternalServerError,
                    errors =
                    listOf(ErrorResponse(message = cause.localizedMessage, path = call.request.path())),
                ),
            )
        }

        exception<RequestValidationException> { call, cause ->
            LOG.error("An error has occurred", cause)

            call.respond(
                status = HttpStatusCode.BadRequest,
                message =
                ErrorHttpResponse(
                    statusCode = HttpStatusCode.BadRequest,
                    errors =
                    cause.reasons.map { reason ->
                        ErrorResponse(message = reason, path = call.request.path())
                    }
                )
            )
        }
    }
}

fun handleFailure(
    error: ApplicationError,
    call: ApplicationCall,
): ErrorHttpResponse =
    when (error) {
        is ApplicationError.NotFoundError ->
            ErrorHttpResponse(
                statusCode = HttpStatusCode.NotFound,
                errors = listOf(ErrorResponse(message = error.message, path = call.request.path())),
            )

        is OptimisticRepositoryError ->
            ErrorHttpResponse(
                statusCode = HttpStatusCode.Conflict,
                errors = listOf(ErrorResponse(message = error.message, path = call.request.path())),
            )

        else -> {
            LOG.error("An error has occurred: ${error.message}", error.cause)
            ErrorHttpResponse(
                statusCode = HttpStatusCode.InternalServerError,
                errors = listOf(ErrorResponse(message = error.message, path = call.request.path()))
            )
        }
    }