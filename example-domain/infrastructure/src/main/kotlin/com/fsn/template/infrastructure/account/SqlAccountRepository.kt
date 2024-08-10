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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.update

object AccountTable : IdTable<String>("accounts") {
  override val id = varchar("id", 50).entityId()
  val ownerName = varchar("ownerName", 100)
  val balance = decimal("balance", 65, 8)
  val currencyCode = varchar("currency_code", 3)
  val createdDateTime = datetime("created_date_time").defaultExpression(CurrentDateTime)
  val updatedDateTime = datetime("updated_date_time").defaultExpression(CurrentDateTime)
  val version = long("version").default(1)

  override val primaryKey = PrimaryKey(id, name = "pk_accounts_id")
}

class SqlAccountRepository : AccountRepository {

  context(Raise<ApplicationError>)
  override suspend fun getAccount(id: AccountId): Account = withContext(Dispatchers.IO) {
    catch({
      AccountTable.select(AccountTable.id eq id.value.toString())
        .firstOrNull()?.toAccountDomain()
        ?: raise(AccountNotFoundError(id))
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
  }

  context(Raise<ApplicationError>)
  override suspend fun createAccount(account: Account): Unit = withContext(Dispatchers.IO) {
    catch({
      AccountTable.insert {
        it[id] = account.id.value.toString()
        it[ownerName] = account.ownerName
        it[balance] = account.balance
        it[currencyCode] = account.currency.currencyCode
        it[updatedDateTime] = localDateTimeUtcNow().toKotlinLocalDateTime()
      }
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
  }

  context(Raise<ApplicationError>)
  override suspend fun updateAccount(account: Account): Unit = withContext(Dispatchers.IO) {
    catch({
      AccountTable.update {
        it[ownerName] = account.ownerName
        it[balance] = account.balance
        it[currencyCode] = account.currency.currencyCode
        it[updatedDateTime] = localDateTimeUtcNow().toKotlinLocalDateTime()
      }
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
  }
}

private fun ResultRow.toAccountDomain() =
  Account(
    id = AccountId.fromString(this[AccountTable.id].value),
    ownerName = this[AccountTable.ownerName],
    balance = this[AccountTable.balance],
    currency = Currency.getInstance(this[AccountTable.currencyCode]),
    createdDateTime = this[AccountTable.createdDateTime].toInstant(TimeZone.UTC).toJavaInstant(),
    version = this[AccountTable.version]
  )
