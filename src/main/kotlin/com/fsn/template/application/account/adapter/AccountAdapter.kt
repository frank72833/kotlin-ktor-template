package com.fsn.template.application.account.adapter

import com.fsn.template.application.account.adapter.request.CreateAccountRequest
import com.fsn.template.application.account.adapter.response.CreateAccountResponse
import com.fsn.template.application.account.adapter.response.toCreateAccountResponse
import com.fsn.template.application.account.service.AccountService
import java.util.UUID

class AccountAdapter(private val accountService: AccountService) {
  suspend fun createAccount(request: CreateAccountRequest): CreateAccountResponse =
    accountService.createAccount(request).toCreateAccountResponse()

  suspend fun getAccount(id: String) =
    accountService.getAccount(UUID.fromString(id))?.toCreateAccountResponse()
}
