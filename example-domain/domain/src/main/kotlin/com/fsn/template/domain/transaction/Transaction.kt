package com.fsn.template.domain.transaction

import com.fsn.template.domain.account.AccountId
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

@JvmInline
value class TransactionId(val value: UUID) {
  companion object {
    fun fromString(id: String): TransactionId = TransactionId(UUID.fromString(id))
  }
}

data class Transaction(
  val id: TransactionId,
  val fromAccountId: AccountId,
  val toAccountId: AccountId,
  val amount: BigDecimal,
  val currency: Currency,
  val businessDateTime: Instant,
)
