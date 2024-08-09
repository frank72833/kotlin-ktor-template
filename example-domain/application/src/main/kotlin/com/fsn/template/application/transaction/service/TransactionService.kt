package com.fsn.template.application.transaction.service

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionRepository
import com.fsn.template.domain.transaction.command.CreateTransactionCommand

class TransactionService(private val repository: TransactionRepository) {
    context(Raise<ApplicationError>)
    suspend fun createTransaction(command: CreateTransactionCommand): Transaction =
        repository.createTransaction(command.toDomain())

    context(Raise<ApplicationError>)
    suspend fun getTransactions(accountId: AccountId): List<Transaction> =
        repository.getTransactions(accountId)
}