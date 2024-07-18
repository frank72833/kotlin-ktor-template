package com.fsn.template.domain.account

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID

@JvmInline
value class AccountId(val value: UUID) {
  companion object {
    fun fromString(id: String): AccountId = AccountId(UUID.fromString(id))
  }
}

data class Account(
  val id: AccountId,
  val ownerName: String,
  val balance: BigDecimal,
  val currency: Currency,
  val createdDateTime: LocalDateTime? = null,
)
