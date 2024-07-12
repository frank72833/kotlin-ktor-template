package com.fsn.template.infrastructure.account

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.AccountNotFoundError
import com.fsn.template.domain.account.AccountRepository
import com.fsn.template.domain.account.GenericAccountRepositoryError
import java.util.Currency
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.jooq.generated.tables.records.AccountsRecord
import org.jooq.generated.tables.references.ACCOUNTS
import org.jooq.kotlin.coroutines.transactionCoroutine

class SqlAccountRepository(private val dslContext: DSLContext) : AccountRepository {

  context(Raise<ApplicationError>)
  override suspend fun getAccount(id: AccountId): Account =
    catch({
      dslContext
        .selectFrom(ACCOUNTS)
        .where(ACCOUNTS.ID.eq(id.value.toString()))
        .awaitFirstOrNull()
        ?.toDomain() ?: raise(AccountNotFoundError(id))
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }

  context(Raise<ApplicationError>)
  override suspend fun upsertAccount(account: Account): Account =
    catch({
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
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
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
