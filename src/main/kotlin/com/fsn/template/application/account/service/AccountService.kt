package com.fsn.template.application.account.service

import com.fsn.template.application.account.adapter.request.CreateAccountRequest
import com.fsn.template.domain.account.Account
import com.fsn.template.domain.account.AccountRepository
import java.util.UUID

class AccountService(private val accountRepository: AccountRepository) {
    suspend fun createAccount(request: CreateAccountRequest): Account =
        accountRepository.createAccount(request.toDomain())

    suspend fun getAccount(id: UUID) : Account? =
        accountRepository.getAccount(id)
}