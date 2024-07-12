package com.fsn.template.domain.account.command

import com.fsn.template.domain.account.Account
import java.math.BigDecimal
import java.util.UUID

class UpdateAccountCommand(val id: UUID, val ownerName: String, val balance: BigDecimal) {
  fun updateDomain(account: Account): Account =
    account.copy(ownerName = ownerName, balance = balance)
}
