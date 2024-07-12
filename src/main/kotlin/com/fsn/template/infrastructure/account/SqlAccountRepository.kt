package com.fsn.template.infrastructure.account

import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.AccountRepository
import java.util.Currency
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.jooq.generated.tables.records.AccountsRecord
import org.jooq.generated.tables.references.ACCOUNTS
import org.jooq.kotlin.coroutines.transactionCoroutine

class SqlAccountRepository(private val dslContext: DSLContext) : AccountRepository {
  override suspend fun getAccount(id: AccountId): Account? =
    dslContext
      .selectFrom(ACCOUNTS)
      .where(ACCOUNTS.ID.eq(id.value.toString()))
      .awaitFirstOrNull()
      ?.toDomain()

  override suspend fun upsertAccount(account: Account): Account =
    dslContext.transactionCoroutine { config ->
      config
        .dsl()
        .insertInto(ACCOUNTS)
        .set(account.toEntity())
        .onDuplicateKeyUpdate()
        .set(account.toEntity())
        .execute()
      account
    }
}

fun Account.toEntity() =
  AccountsRecord(
    id = id.value.toString(),
    ownername = ownerName,
    balance = balance,
    currencyCode = currency.currencyCode,
    createdDateTime = createdDateTime ?: localDateTimeUtcNow(),
    updatedDateTime = localDateTimeUtcNow(),
  )

fun AccountsRecord.toDomain() =
  Account(
    id = AccountId.fromString(id),
    ownerName = ownername,
    balance = balance,
    currency = Currency.getInstance(currencyCode),
    createdDateTime = createdDateTime,
  )
