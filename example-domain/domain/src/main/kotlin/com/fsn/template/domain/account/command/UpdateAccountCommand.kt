package com.fsn.template.domain.account.command

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId

data class UpdateAccountCommand(
  val accountId: AccountId,
  val ownerName: String,
) {
  fun updateDomain(account: Account): Account =
    account.copy(ownerName = ownerName)
}
