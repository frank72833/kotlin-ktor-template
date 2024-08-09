package com.fsn.template.application.configuration

import com.fsn.template.core.getLogger
import com.fsn.template.infrastructure.account.SqlAccountRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond

private var LOG = getLogger<SqlAccountRepository>()

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
            statusCode = HttpStatusCode.InternalServerError,
            errors =
              cause.reasons.map {
                ErrorResponse(message = cause.localizedMessage, path = call.request.path())
              }
          )
      )
    }
  }
}
