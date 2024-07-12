package com.fsn.template.application.account.adapter.request

import io.ktor.server.plugins.requestvalidation.RequestValidationException
import java.util.UUID

@JvmInline
value class RequestAccountId private constructor(val id: UUID) {
  companion object {
    fun fromString(id: String) =
      try {
        RequestAccountId(UUID.fromString(id))
      } catch (e: Exception) {
        throw RequestValidationException(id, listOf("Invalid Account Id: $id"))
      }
  }
}
