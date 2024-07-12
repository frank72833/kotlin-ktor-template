package com.fsn.template.domain.account

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID

data class Account(
  val id: UUID,
  val ownerName: String,
  val balance: BigDecimal,
  val currency: Currency,
  val createdDateTime: LocalDateTime? = null,
)
