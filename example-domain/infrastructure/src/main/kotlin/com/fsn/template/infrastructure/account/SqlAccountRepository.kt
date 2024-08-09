package com.fsn.template.infrastructure.account

import arrow.core.raise.Raise
import arrow.core.raise.catch
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.core.toLocalDateTimeUtc
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
import java.time.ZoneOffset

class SqlAccountRepository(private val dslContext: DSLContext) : AccountRepository {

  context(Raise<ApplicationError>)
  override suspend fun getAccount(id: AccountId): Account =
    catch({
      dslContext.transactionCoroutine { config ->
        config.dsl()
          .selectFrom(ACCOUNTS)
          .where(ACCOUNTS.ID.eq(id.value.toString()))
          .awaitFirstOrNull()
          ?.toDomain() ?: raise(AccountNotFoundError(id))
      }
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }

  context(Raise<ApplicationError>)
  override suspend fun createAccount(account: Account): Account =
    catch({
      dslContext.transactionCoroutine { config ->
        val accountEntity = account.toEntity()
        accountEntity.attach(config)
        accountEntity.store()
        accountEntity.toDomain()
      }
    }) { exception ->
        raise(GenericAccountRepositoryError(exception))
    }

  context(Raise<ApplicationError>)
  override suspend fun updateAccount(account: Account): Account =
    catch({
      dslContext.transactionCoroutine { config ->
        val accountEntity = account.toEntity()
        accountEntity.attach(config)
        accountEntity.update()
        accountEntity.toDomain()
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
}

fun Account.toEntity(): AccountsRecord =
  AccountsRecord(
    id = id.value.toString(),
    ownerName = ownerName,
    balance = balance,
    currencyCode = currency.currencyCode,
    createdDateTime = createdDateTime?.toLocalDateTimeUtc() ?: localDateTimeUtcNow(),
    updatedDateTime = localDateTimeUtcNow(),
    version = version ?: 1L
  )

fun AccountsRecord.toDomain() =
  Account(
    id = AccountId.fromString(id),
    ownerName = ownerName,
    balance = balance,
    currency = Currency.getInstance(currencyCode),
    createdDateTime = createdDateTime.toInstant(ZoneOffset.UTC),
    version = version
  )
