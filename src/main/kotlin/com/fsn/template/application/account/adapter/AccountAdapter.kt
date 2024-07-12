package com.fsn.template.application.account.adapter

import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.RequestAccountId
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.account.adapter.response.CreateAccountResponse
import com.fsn.template.application.account.adapter.response.toCreateAccountResponse
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.domain.account.AccountId

class AccountAdapter(private val accountService: AccountService) {
  suspend fun createAccount(request: CreateAccountApiRequest): CreateAccountResponse =
    accountService.createAccount(request.toDomainCommand()).toCreateAccountResponse()

  suspend fun updateAccount(
    accountId: RequestAccountId,
    request: UpdateAccountApiRequest,
  ): CreateAccountResponse =
    accountService.updateAccount(request.toDomainCommand(accountId.id)).toCreateAccountResponse()

  suspend fun getAccount(accountId: RequestAccountId) =
    accountService.getAccount(AccountId(accountId.id))?.toCreateAccountResponse()
}
