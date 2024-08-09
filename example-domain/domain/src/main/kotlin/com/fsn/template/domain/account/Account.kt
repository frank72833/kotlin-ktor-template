package com.fsn.template.domain.account

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

@JvmInline
value class AccountId(val value: UUID) {
    companion object {
        fun fromString(id: String): AccountId = AccountId(UUID.fromString(id))
    }
}

data class Account(
    val id: AccountId,
    val ownerName: String,
    val balance: BigDecimal,
    val currency: Currency,
    val createdDateTime: Instant? = null,
    val version: Long? = null
)

context(Raise<ApplicationError>)
fun Account.deposit(amount: BigDecimal, currency: Currency): Account {
    if (this.currency != currency) {
        raise(InvalidCurrencyAccountError(id))
    }
    return this.copy(balance = this.balance.add(amount))
}

context(Raise<ApplicationError>)
fun Account.withdrawal(amount: BigDecimal, currency: Currency): Account {
    if (this.currency != currency) {
        raise(InvalidCurrencyAccountError(id))
    }

    val remaining = balance.subtract(amount)
    if (BigDecimal.ZERO > remaining) {
        raise(NotEnoughFoundsAccountError(id))
    }

    return this.copy(balance = remaining)
}

data class InvalidCurrencyAccountError(
    val accountId: AccountId,
    override val cause: Throwable? = null,
    override val message: String = "Invalid currency for accountId: ${accountId.value}"
) : ApplicationError.NonRetryableError

data class NotEnoughFoundsAccountError(
    val accountId: AccountId,
    override val cause: Throwable? = null,
    override val message: String = "Not enough funds in accountId: ${accountId.value}"
) : ApplicationError.RetryableError
