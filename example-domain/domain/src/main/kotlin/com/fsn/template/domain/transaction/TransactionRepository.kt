package com.fsn.template.domain.transaction

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId

interface TransactionRepository {
  context(Raise<ApplicationError>)
  suspend fun getTransactions(accountId: AccountId): List<Transaction>

  context(Raise<ApplicationError>)
  suspend fun createTransaction(transaction: Transaction): Transaction
}

data class GenericTransactionRepositoryError(
  override val cause: Throwable?,
  override val message: String = "An error has occurred in Transaction Repository",
) : ApplicationError.NonRetryableError
