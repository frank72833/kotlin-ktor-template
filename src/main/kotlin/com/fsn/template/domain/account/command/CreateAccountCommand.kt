package com.fsn.template.domain.account.command

import com.fsn.template.domain.account.Account
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

class CreateAccountCommand(
  val id: UUID = UUID.randomUUID(),
  val ownerName: String,
  val balance: BigDecimal,
  val currency: Currency,
) {
  fun toDomain(): Account =
    Account(id = id, ownerName = ownerName, balance = balance, currency = currency)
}
