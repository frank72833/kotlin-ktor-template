package com.fsn.template.application.account.adapter

import arrow.core.raise.Raise
import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.account.adapter.response.AccountResponse
import com.fsn.template.application.account.adapter.response.toCreateAccountResponse
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.core.account.RequestAccountId
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import io.ktor.http.HttpStatusCode

class AccountAdapter(private val accountService: AccountService) {

  context(Raise<ApplicationError>)
  suspend fun createAccount(request: CreateAccountApiRequest): AccountResponse =
    accountService
      .createAccount(request.toDomainCommand())
      .toCreateAccountResponse(HttpStatusCode.Created)

  context(Raise<ApplicationError>)
  suspend fun updateAccount(
    accountId: RequestAccountId,
    request: UpdateAccountApiRequest,
  ): AccountResponse =
    accountService
      .updateAccount(request.toDomainCommand(accountId.id))
      .toCreateAccountResponse(HttpStatusCode.OK)

  context(Raise<ApplicationError>)
  suspend fun getAccount(accountId: RequestAccountId): AccountResponse =
    accountService.getAccount(AccountId(accountId.id)).toCreateAccountResponse(HttpStatusCode.Found)
}
