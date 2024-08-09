package com.fsn.template.application.transaction.adapter

import arrow.core.raise.Raise
import com.fsn.template.application.transaction.adapter.request.CreateTransactionApiRequest
import com.fsn.template.application.transaction.adapter.response.CreateTransactionResponse
import com.fsn.template.application.transaction.adapter.response.GetTransactionResponse
import com.fsn.template.application.transaction.adapter.response.toCreateTransactionResponse
import com.fsn.template.application.transaction.adapter.response.toGetTransactionResponse
import com.fsn.template.application.transaction.service.TransactionService
import com.fsn.template.core.account.RequestAccountId
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import com.fsn.template.infrastructure.configuration.runInTransaction
import io.ktor.http.HttpStatusCode

class TransactionAdapter(private val transactionService: TransactionService) {

    context(Raise<ApplicationError>)
    suspend fun createTransaction(request: CreateTransactionApiRequest): CreateTransactionResponse = runInTransaction {
            transactionService.createTransaction(request.toCreateTransactionCommand())
                .toCreateTransactionResponse(HttpStatusCode.Created)
        }

    context(Raise<ApplicationError>)
    suspend fun getTransactions(accountId: RequestAccountId): GetTransactionResponse =
        transactionService.getTransactions(AccountId(accountId.id))
            .toGetTransactionResponse(HttpStatusCode.Found)
}