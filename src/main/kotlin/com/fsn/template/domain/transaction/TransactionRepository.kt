package com.fsn.template.domain.transaction

import java.util.UUID

interface TransactionRepository {
    suspend fun getTransactions(fromAccountId: UUID): List<Transaction>
    suspend fun addTransaction(transaction: Transaction): Transaction
}