package com.fsn.template.infrastructure.account

import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import java.util.Currency
import java.util.UUID
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.jooq.generated.tables.records.AccountsRecord
import org.jooq.generated.tables.references.ACCOUNTS
import org.jooq.kotlin.coroutines.transactionCoroutine

class SqlAccountRepository(private val dslContext: DSLContext) : AccountRepository {
  override suspend fun getAccount(id: UUID): Account? =
    dslContext
      .selectFrom(ACCOUNTS)
      .where(ACCOUNTS.ID.eq(id.toString()))
      .awaitFirstOrNull()
      ?.toDomain()

  override suspend fun createAccount(account: Account): Account =
    dslContext.transactionCoroutine { config ->
      config.dsl().newRecord(ACCOUNTS, account.toEntity()).store()
      account
    }
}

fun Account.toEntity() =
  AccountsRecord(
    id = this.id.toString(),
    ownername = this.ownerName,
    balance = this.balance,
    currencyCode = this.currency.currencyCode,
    createdDateTime = createdDateTime ?: localDateTimeUtcNow(),
    updatedDateTime = localDateTimeUtcNow(),
  )

fun AccountsRecord.toDomain() =
  Account(
    id = UUID.fromString(id),
    ownerName = ownername,
    balance = balance,
    currency = Currency.getInstance(currencyCode),
    createdDateTime = createdDateTime,
  )
