package com.fsn.template.domain.account.command

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import java.math.BigDecimal

data class UpdateAccountCommand(
  val accountId: AccountId,
  val ownerName: String,
  val balance: BigDecimal,
) {
  fun updateDomain(account: Account): Account =
    account.copy(ownerName = ownerName, balance = balance)
}
