package com.fsn.template.application.account.adapter.request

import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.command.UpdateAccountCommand
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountApiRequest(
  val ownerName: String? = null,
) {

  fun toDomainCommand(id: UUID): UpdateAccountCommand =
    UpdateAccountCommand(
      accountId = AccountId(id),
      ownerName = ownerName!!
    )

  fun validate(): ValidationResult {
    val reasonsList = ArrayList<String>()
    if (ownerName.isNullOrBlank()) {
      reasonsList.add("Owner name cannot be empty")
    } else {
      if (ownerName.length > 100) reasonsList.add("Owner name cannot longer than 100 characters")
    }

    return if (reasonsList.isEmpty()) ValidationResult.Valid
    else ValidationResult.Invalid(reasonsList)
  }
}
