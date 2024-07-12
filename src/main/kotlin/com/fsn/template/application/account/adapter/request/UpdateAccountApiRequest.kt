package com.fsn.template.application.account.adapter.request

import com.fsn.template.domain.account.command.UpdateAccountCommand
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountApiRequest(val ownerName: String, val balance: String) {

  fun toDomainCommand(id: String): UpdateAccountCommand =
    UpdateAccountCommand(
      id = UUID.fromString(id),
      ownerName = ownerName,
      balance = balance.toBigDecimal(),
    )

  fun validate(): ValidationResult {
    val reasonsList = ArrayList<String>()
    if (ownerName.isBlank()) reasonsList.add("Owner name cannot be empty")

    if (ownerName.length > 100) reasonsList.add("Owner name cannot longer than 100 characters")

    if (balance.toBigDecimalOrNull() == null) reasonsList.add("Balance has to be a number")

    return if (reasonsList.isEmpty()) ValidationResult.Valid
    else ValidationResult.Invalid(reasonsList)
  }
}
