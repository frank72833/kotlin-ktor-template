package com.fsn.template.application.transaction.service

import arrow.core.raise.Raise
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.GenericTransactionRepositoryError
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionRepository
import com.fsn.template.domain.transaction.command.CreateTransactionCommand
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class TransactionService(
    private val accountService: AccountService,
    private val repository: TransactionRepository
) {
    context(Raise<ApplicationError>)
    suspend fun createTransaction(command: CreateTransactionCommand): Transaction {
        accountService.updateAccountBalances(
            fromAccountId = command.fromAccountId,
            toAccountId = command.toAccountId,
            amount = command.amount,
            currency = command.currency)

        // Fake operation taking way too long
        delay(30.seconds)
        //raise(GenericTransactionRepositoryError(null))
        throw RuntimeException("BOOOM!")
        return repository.createTransaction(command.toDomain())
    }

    context(Raise<ApplicationError>)
    suspend fun getTransactions(accountId: AccountId): List<Transaction> =
        repository.getTransactions(accountId)
}