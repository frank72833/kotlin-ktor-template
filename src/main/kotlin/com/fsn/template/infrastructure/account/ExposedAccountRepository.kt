package com.fsn.template.infrastructure.account

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import com.fsn.template.infrastructure.utils.suspendTransaction
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID

object AccountTable : IdTable<String>("accounts") {
    override val id = varchar("id", 50).entityId()
    val ownerName = varchar("ownerName", 100)
    val balance = decimal("balance", 65, 8)
    val currencyCode = varchar("currency_code", 3)
    val createdDateTime = datetime("created_date_time").defaultExpression(CurrentDateTime)
    val updatedDateTime = datetime("updated_date_time").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id, name = "pk_accounts_id")
}

class AccountEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, AccountEntity>(AccountTable)

    var ownerName by AccountTable.ownerName
    var balance by AccountTable.balance
    var currencyCode by AccountTable.currencyCode
    val createdDateTime by AccountTable.createdDateTime // Cannot be reassigned
    var updatedDateTime by AccountTable.updatedDateTime

    fun toDomain(): Account = Account(
        id = UUID.fromString(id.value),
        ownerName = ownerName,
        balance = balance,
        currency = Currency.getInstance(currencyCode)
    )
}

class ExposedAccountRepository: AccountRepository {
    override suspend fun getAccount(id: UUID): Account? = suspendTransaction {
        AccountEntity.findById(id.toString())?.toDomain()
    }

    override suspend fun createAccount(account: Account): Account = suspendTransaction {
        AccountEntity.new(account.id.toString()) {
            ownerName = account.ownerName
            balance = account.balance
            currencyCode = account.currency.currencyCode
            updatedDateTime = LocalDateTime.now()
        }.toDomain()
    }
}