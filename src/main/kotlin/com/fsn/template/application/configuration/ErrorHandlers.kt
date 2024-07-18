package com.fsn.template.application.configuration

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond

fun Application.configureErrorHandlers() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
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
      call.respond(
        status = HttpStatusCode.BadRequest,
        message =
          ErrorHttpResponse(
            statusCode = HttpStatusCode.InternalServerError,
            errors =
              cause.reasons.map {
                ErrorResponse(message = cause.localizedMessage, path = call.request.path())
              },
          ),
      )
    }
  }
}
