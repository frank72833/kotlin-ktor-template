package com.fsn.template.application.account.adapter.request

import com.fsn.template.core.currencyExists
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.command.CreateAccountCommand
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.util.Currency
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountApiRequest(
  val ownerName: String? = null,
  val currencyCode: String? = null,
) {
  fun toDomainCommand(): CreateAccountCommand =
    CreateAccountCommand(
      accountId = AccountId(UUID.randomUUID()),
      ownerName = ownerName!!,
      currency = Currency.getInstance(currencyCode!!),
    )

  fun validate(): ValidationResult {
    val reasonsList = ArrayList<String>()
    if (ownerName.isNullOrBlank()) {
      reasonsList.add("Owner name cannot be empty")
    } else {
      if (ownerName.length > 100) reasonsList.add("Owner name cannot longer than 100 characters")
    }

    if (currencyCode.isNullOrBlank()) {
      reasonsList.add("Currency code cannot be empty")
    } else {
      if (!currencyExists(currencyCode)) reasonsList.add("Currency does not exists")
    }

    return if (reasonsList.isEmpty()) ValidationResult.Valid
    else ValidationResult.Invalid(reasonsList)
  }
}
