package com.fsn.template.infrastructure.account

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

class InMemAccountRepository: AccountRepository {

    private val accounts = mutableMapOf(
        UUID.fromString("72291c34-8be2-463e-b714-62d08fbc46af") to Account(
            id = UUID.fromString("72291c34-8be2-463e-b714-62d08fbc46af"),
            ownerName = "Fran",
            balance = BigDecimal("122343232.23"),
            currency = Currency.getInstance("EUR")
        )
    );

    override suspend fun getAccount(id: UUID): Account? =
        accounts[id]

    override suspend fun createAccount(account: Account): Account =
        accounts.put(account.id, account) ?: account
}