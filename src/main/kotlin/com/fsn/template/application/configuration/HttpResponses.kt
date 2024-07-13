package com.fsn.template.application.configuration

import io.ktor.http.HttpStatusCode
import java.time.Instant
import kotlinx.serialization.Serializable

@Serializable
sealed interface HttpResponse {
  val status: String
  val statusCode: HttpStatusCode

  @Serializable
  data class ErrorHttpResponse(
    override val status: String = "error",
    @Serializable(with = HttpStatusCodeSerializer::class) override val statusCode: HttpStatusCode,
    val errors: List<ErrorResponse>,
  ) : HttpResponse
}

@Serializable
data class ErrorResponse(
  val message: String,
  @Serializable(with = InstantSerializer::class) val timestamp: Instant = Instant.now(),
  val path: String,
)
