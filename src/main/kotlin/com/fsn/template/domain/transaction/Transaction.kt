package com.fsn.template.domain.transaction

import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

data class Transaction(
  val id: UUID,
  val fromAccountId: UUID,
  val toAccountId: UUID,
  val amount: BigDecimal,
  val currency: Currency,
)
