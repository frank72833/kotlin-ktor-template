package com.fsn.template.application.account.service

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.account.AccountRepository
import com.fsn.template.domain.account.command.CreateAccountCommand
import com.fsn.template.domain.account.command.UpdateAccountCommand
import org.slf4j.LoggerFactory

private var LOG = LoggerFactory.getLogger(AccountService::class.java)

class AccountService(private val accountRepository: AccountRepository) {
  context(Raise<ApplicationError>)
  suspend fun createAccount(request: CreateAccountCommand): Account {
    LOG.info("Creating account: $request")
    return accountRepository.upsertAccount(request.toDomain())
  }

  context(Raise<ApplicationError>)
  suspend fun updateAccount(request: UpdateAccountCommand): Account {
    LOG.info("Updating account: $request")
    val account = getAccount(request.accountId)
    return accountRepository.upsertAccount(request.updateDomain(account))
  }

  context(Raise<ApplicationError>)
  suspend fun getAccount(id: AccountId): Account = accountRepository.getAccount(id)
}
