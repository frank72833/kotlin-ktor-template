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
import com.fsn.template.infrastructure.configuration.OptimisticRepositoryError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Currency
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

object AccountsTable : IdTable<String>("accounts") {
  override val id = varchar("id", 50).entityId()
  val ownerName = varchar("owner_name", 100)
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
      AccountsTable.selectAll()
        .where { AccountsTable.id eq id.value.toString() }
        .firstOrNull()?.toAccountDomain()
        ?: raise(AccountNotFoundError(id))
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
  }

  context(Raise<ApplicationError>)
  override suspend fun createAccount(account: Account): Unit = withContext(Dispatchers.IO) {
    catch({
      AccountsTable.insert {
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
      val currentVersion = account.version ?: 0
      val updatedRows = AccountsTable.update({ (AccountsTable.id eq account.id.value.toString()) and (AccountsTable.version eq currentVersion) }) {
        it[ownerName] = account.ownerName
        it[balance] = account.balance
        it[currencyCode] = account.currency.currencyCode
        it[updatedDateTime] = localDateTimeUtcNow().toKotlinLocalDateTime()
        it[version] = currentVersion + 1
      }

      if (updatedRows == 0) {
        raise(OptimisticRepositoryError(id = account.id, clazz = Account::class.simpleName!!))
      }
    }) { exception ->
      raise(GenericAccountRepositoryError(exception))
    }
  }
}

private fun ResultRow.toAccountDomain() =
  Account(
    id = AccountId.fromString(this[AccountsTable.id].value),
    ownerName = this[AccountsTable.ownerName],
    balance = this[AccountsTable.balance],
    currency = Currency.getInstance(this[AccountsTable.currencyCode]),
    createdDateTime = this[AccountsTable.createdDateTime].toInstant(TimeZone.UTC).toJavaInstant(),
    version = this[AccountsTable.version]
  )
