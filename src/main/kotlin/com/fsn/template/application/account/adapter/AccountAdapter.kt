package com.fsn.template.application.account.adapter

import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.account.adapter.response.CreateAccountResponse
import com.fsn.template.application.account.adapter.response.toCreateAccountResponse
import com.fsn.template.application.account.service.AccountService
import java.util.UUID

class AccountAdapter(private val accountService: AccountService) {
  suspend fun createAccount(request: CreateAccountApiRequest): CreateAccountResponse =
    accountService.createAccount(request.toDomainCommand()).toCreateAccountResponse()

  suspend fun updateAccount(id: String, request: UpdateAccountApiRequest): CreateAccountResponse =
    accountService.updateAccount(request.toDomainCommand(id)).toCreateAccountResponse()

  suspend fun getAccount(id: String) =
    accountService.getAccount(UUID.fromString(id))?.toCreateAccountResponse()
}
