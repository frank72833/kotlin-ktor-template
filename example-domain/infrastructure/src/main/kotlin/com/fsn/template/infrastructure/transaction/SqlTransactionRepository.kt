package com.fsn.template.infrastructure.transaction

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.GenericTransactionRepositoryError
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionId
import com.fsn.template.domain.transaction.TransactionRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import java.util.Currency

object TransactionsTable : IdTable<String>("transactions") {
    override val id = TransactionsTable.varchar("id", 50).entityId()
    val fromAccountId = TransactionsTable.varchar("from_account_id", 50)
    val toAccountId = TransactionsTable.varchar("to_account_id", 50)
    val amount = TransactionsTable.decimal("amount", 65, 8)
    val currencyCode = TransactionsTable.varchar("currency_code", 3)
    val businessDateTime = datetime("business_date_time")
    val createdDateTime = datetime("created_date_time").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id, name = "pk_transactions_id")
}

class SqlTransactionRepository: TransactionRepository {
    context(Raise<ApplicationError>)
    override suspend fun getTransactions(accountId: AccountId): List<Transaction> =
        catch({
            val accountIdString = accountId.value.toString()
            TransactionsTable.selectAll()
                .where {
                    (TransactionsTable.fromAccountId eq accountIdString)
                        .or(TransactionsTable.toAccountId eq accountIdString)
                }.map { it.toTransactionDomain() }
        }) { exception -> raise(GenericTransactionRepositoryError(exception)) }

    context(Raise<ApplicationError>)
    override suspend fun createTransaction(transaction: Transaction): Unit =
        catch({
            TransactionsTable.insert {
                it[id] = transaction.id.value.toString()
                it[fromAccountId] = transaction.fromAccountId.value.toString()
                it[toAccountId] = transaction.toAccountId.value.toString()
                it[amount] = transaction.amount
                it[currencyCode] = transaction.currency.currencyCode
                it[businessDateTime] = transaction.businessDateTime.toKotlinInstant().toLocalDateTime(TimeZone.UTC)
            }
        }) { exception -> raise(GenericTransactionRepositoryError(exception)) }
}

private fun ResultRow.toTransactionDomain() : Transaction =
    Transaction(
        id = TransactionId.fromString(this[TransactionsTable.id].value),
        fromAccountId = AccountId.fromString(this[TransactionsTable.fromAccountId]),
        toAccountId = AccountId.fromString(this[TransactionsTable.toAccountId]),
        amount = this[TransactionsTable.amount],
        currency = Currency.getInstance(this[TransactionsTable.currencyCode]),
        businessDateTime = this[TransactionsTable.businessDateTime].toInstant(TimeZone.UTC).toJavaInstant()
    )