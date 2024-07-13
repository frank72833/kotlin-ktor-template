package com.fsn.template.application.configuration

import io.ktor.http.HttpStatusCode
import java.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ErrorHttpResponse(
  val status: String = "error",
  @Serializable(with = HttpStatusCodeSerializer::class) val statusCode: HttpStatusCode,
  val errors: List<ErrorResponse>,
)

@Serializable
data class ErrorResponse(
  val message: String,
  @Serializable(with = InstantSerializer::class) val timestamp: Instant = Instant.now(),
  val path: String,
)
