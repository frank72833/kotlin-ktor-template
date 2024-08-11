package com.fsn.template.application.account.service

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.core.getLogger
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.AccountRepository
import com.fsn.template.domain.account.command.CreateAccountCommand
import com.fsn.template.domain.account.command.UpdateAccountCommand
import com.fsn.template.domain.account.deposit
import com.fsn.template.domain.account.withdrawal
import java.math.BigDecimal
import java.util.Currency

private var LOG = getLogger<AccountService>()

class AccountService(private val accountRepository: AccountRepository) {
    context(Raise<ApplicationError>)
    suspend fun createAccount(request: CreateAccountCommand): Account {
        LOG.info("Creating account: $request")
        val account = request.toDomain()
        accountRepository.createAccount(account)
        return account
    }

    context(Raise<ApplicationError>)
    suspend fun updateAccount(request: UpdateAccountCommand): Account {
        LOG.info("Updating account: $request")
        val account = getAccount(request.accountId)
        val accountUpdate = request.updateDomain(account)
        accountRepository.updateAccount(accountUpdate)
        return accountUpdate
    }

    context(Raise<ApplicationError>)
    suspend fun getAccount(id: AccountId): Account = accountRepository.getAccount(id)

    context(Raise<ApplicationError>)
    suspend fun updateAccountBalances(
        fromAccountId: AccountId,
        toAccountId: AccountId,
        amount: BigDecimal,
        currency: Currency
    ) {
        val fromAccount = getAccount(fromAccountId)
        val toAccount = getAccount(toAccountId)

        val fromAccountUpdate = fromAccount.withdrawal(amount, currency)
        val toAccountUpdate = toAccount.deposit(amount, currency)

        accountRepository.updateAccount(fromAccountUpdate)
        accountRepository.updateAccount(toAccountUpdate)
    }
}
