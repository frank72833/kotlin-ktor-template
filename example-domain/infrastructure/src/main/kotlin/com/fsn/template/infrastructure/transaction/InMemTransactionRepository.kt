package com.fsn.template.infrastructure.transaction

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionId
import com.fsn.template.domain.transaction.TransactionRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

private val transactions =
  mutableListOf(
    Transaction(
      id = TransactionId(UUID.fromString("de39d715-4b5e-4edb-9c15-5b4aeccedba9")),
      fromAccountId = AccountId(UUID.fromString("3bef3359-4f30-435a-82de-2f486d4be505")),
      toAccountId = AccountId(UUID.fromString("bc2b81e4-dbe6-4247-b963-11eb72c6ee03")),
      amount = BigDecimal("354.22"),
      currency = Currency.getInstance("EUR"),
      businessDateTime = Instant.now(),
    )
  )

class InMemTransactionRepository : TransactionRepository {

  context(Raise<ApplicationError>)
  override suspend fun getTransactions(accountId: AccountId): List<Transaction> =
    transactions
      .filter { it.fromAccountId == accountId || it.toAccountId == accountId }

  context(Raise<ApplicationError>)
  override suspend fun createTransaction(transaction: Transaction): Transaction {
    transactions.add(transaction)
    return transaction
  }
}
