package com.fsn.template.domain.account.command

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

class CreateAccountCommand(
  val accountId: AccountId = AccountId(UUID.randomUUID()),
  val ownerName: String,
  val balance: BigDecimal,
  val currency: Currency,
) {
  fun toDomain(): Account =
    Account(id = accountId, ownerName = ownerName, balance = balance, currency = currency)
}
