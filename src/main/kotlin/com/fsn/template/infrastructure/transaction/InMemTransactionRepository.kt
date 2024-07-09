package com.fsn.template.infrastructure.transaction

import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionRepository
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

class InMemTransactionRepository: TransactionRepository {
    private val transactions = mutableListOf(
        Transaction(
            id = UUID.fromString("de39d715-4b5e-4edb-9c15-5b4aeccedba9"),
            fromAccountId = UUID.fromString("3bef3359-4f30-435a-82de-2f486d4be505"),
            toAccountId = UUID.fromString("bc2b81e4-dbe6-4247-b963-11eb72c6ee03"),
            amount = BigDecimal("354.22"),
            currency = Currency.getInstance("EUR")
        )
    )

    override suspend fun getTransactions(fromAccountId: UUID): List<Transaction> =
        transactions.filter { it.fromAccountId == fromAccountId }

    override suspend fun addTransaction(transaction: Transaction): Transaction {
        transactions.add(transaction)
        return transaction
    }
}