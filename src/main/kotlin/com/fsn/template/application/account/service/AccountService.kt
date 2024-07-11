package com.fsn.template.application.account.service

import com.fsn.template.application.account.adapter.request.CreateAccountRequest
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import org.slf4j.LoggerFactory
import java.util.UUID

private var LOG = LoggerFactory.getLogger(AccountService::class.java)

class AccountService(private val accountRepository: AccountRepository) {
    suspend fun createAccount(request: CreateAccountRequest): Account {
        LOG.info("Creating account: $request")
        return accountRepository.createAccount(request.toDomain())
    }

    suspend fun getAccount(id: UUID) : Account? =
        accountRepository.getAccount(id)
}