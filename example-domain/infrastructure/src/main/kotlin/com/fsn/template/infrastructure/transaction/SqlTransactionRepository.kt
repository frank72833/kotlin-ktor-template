package com.fsn.template.infrastructure.transaction

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.core.toLocalDateTimeUtc
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.GenericTransactionRepositoryError
import com.fsn.template.domain.transaction.Transaction
import com.fsn.template.domain.transaction.TransactionId
import com.fsn.template.domain.transaction.TransactionRepository
import org.jooq.DSLContext
import org.jooq.generated.tables.records.TransactionsRecord
import org.jooq.generated.tables.references.TRANSACTIONS
import org.jooq.kotlin.coroutines.transactionCoroutine
import java.time.ZoneOffset
import java.util.Currency

class SqlTransactionRepository(private val dslContext: DSLContext): TransactionRepository {
    context(Raise<ApplicationError>)
    override suspend fun getTransactions(accountId: AccountId): List<Transaction> =
        catch({
            dslContext.transactionCoroutine { config ->
                config.dsl()
                    .selectFrom(TRANSACTIONS)
                    .where(
                        TRANSACTIONS.FROM_ACCOUNT_ID.eq(accountId.value.toString())
                            .or(TRANSACTIONS.TO_ACCOUNT_ID.eq(accountId.value.toString()))
                        )
                    .fetch()
                    .map { it.toDomain() }
            }
        }) { exception -> raise(GenericTransactionRepositoryError(exception)) }

    context(Raise<ApplicationError>)
    override suspend fun createTransaction(transaction: Transaction): Transaction =
        catch({
            dslContext.transactionCoroutine { config ->
                val entity = transaction.toEntity()
                entity.attach(config)
                entity.insert()
                entity.toDomain()
            }
        }) { exception -> raise(GenericTransactionRepositoryError(exception)) }
}

fun TransactionsRecord.toDomain() : Transaction =
    Transaction(
        id = TransactionId.fromString(id),
        fromAccountId = AccountId.fromString(fromAccountId),
        toAccountId = AccountId.fromString(toAccountId),
        amount = amount,
        currency = Currency.getInstance(currencyCode),
        businessDateTime = businessDateTime.toInstant(ZoneOffset.UTC),
    )

fun Transaction.toEntity(): TransactionsRecord =
    TransactionsRecord(
        id = id.value.toString(),
        fromAccountId = fromAccountId.value.toString(),
        toAccountId = toAccountId.value.toString(),
        amount = amount,
        currencyCode = currency.currencyCode,
        businessDateTime = businessDateTime.toLocalDateTimeUtc(),
        createdDateTime = localDateTimeUtcNow()
    )