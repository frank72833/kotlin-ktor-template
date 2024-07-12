package com.fsn.template.application.account.service

import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import com.fsn.template.domain.account.command.CreateAccountCommand
import com.fsn.template.domain.account.command.UpdateAccountCommand
import io.ktor.server.plugins.NotFoundException
import java.util.UUID
import org.slf4j.LoggerFactory

private var LOG = LoggerFactory.getLogger(AccountService::class.java)

class AccountService(private val accountRepository: AccountRepository) {
  suspend fun createAccount(request: CreateAccountCommand): Account {
    LOG.info("Creating account: $request")
    return accountRepository.upsertAccount(request.toDomain())
  }

  suspend fun updateAccount(request: UpdateAccountCommand): Account {
    LOG.info("Updating account: $request")
    val account = getAccountOrThrow(request.id)
    return accountRepository.upsertAccount(request.updateDomain(account))
  }

  suspend fun getAccount(id: UUID): Account? = accountRepository.getAccount(id)

  private suspend fun getAccountOrThrow(id: UUID): Account =
    accountRepository.getAccount(id) ?: throw NotFoundException("")
}
