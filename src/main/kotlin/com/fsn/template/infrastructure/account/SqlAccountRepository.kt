package com.fsn.template.infrastructure.account

import com.fsn.template.core.localDateTimeUtcNow
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import java.util.Currency
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.generated.tables.records.AccountsRecord
import org.jooq.generated.tables.references.ACCOUNTS

class SqlAccountRepository(private val dslContext: DSLContext) : AccountRepository {
  override suspend fun getAccount(id: UUID): Account? =
    withContext(Dispatchers.IO) {
        dslContext.selectFrom(ACCOUNTS).where(ACCOUNTS.ID.eq(id.toString())).fetchOne()
      }
      ?.toDomain()

  override suspend fun createAccount(account: Account): Account =
    withContext(Dispatchers.IO) {
      dslContext.newRecord(ACCOUNTS, account.toEntity()).store()
      account
    }
}

fun Account.toEntity() =
  AccountsRecord(
    id = this.id.toString(),
    ownername = this.ownerName,
    balance = this.balance,
    currencyCode = this.currency.currencyCode,
    createdDateTime = localDateTimeUtcNow(),
    updatedDateTime = localDateTimeUtcNow(),
  )

fun AccountsRecord.toDomain() =
  Account(
    id = UUID.fromString(id),
    ownerName = ownername,
    balance = balance,
    currency = Currency.getInstance(currencyCode),
  )
