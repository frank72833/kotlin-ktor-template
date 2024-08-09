package com.fsn.template.domain.transaction.command

import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionId
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

data class CreateTransactionCommand(
    val fromAccountId: AccountId,
    val toAccountId: AccountId,
    val amount: BigDecimal,
    val currency: Currency,
) {
    fun toDomain(): Transaction = Transaction(
        id = TransactionId(UUID.randomUUID()),
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        amount = amount,
        currency = currency,
        businessDateTime = Instant.now(),
    )
}
