package com.fsn.template.application.account.adapter

import arrow.core.raise.fold
import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.RequestAccountId
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.account.adapter.response.toCreateAccountResponse
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.application.configuration.ErrorResponse
import com.fsn.template.application.configuration.ErrorsResponse
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.AccountId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import java.time.Instant

class AccountAdapter(private val accountService: AccountService) {

  suspend fun createAccount(request: CreateAccountApiRequest, call: ApplicationCall): Unit =
    fold(
      block = { accountService.createAccount(request.toDomainCommand()).toCreateAccountResponse() },
      recover = { error -> handleFailure(error, call) },
      transform = { call.respond(it) },
    )

  suspend fun updateAccount(
    accountId: RequestAccountId,
    request: UpdateAccountApiRequest,
    call: ApplicationCall,
  ): Unit =
    fold(
      block = {
        accountService
          .updateAccount(request.toDomainCommand(accountId.id))
          .toCreateAccountResponse()
      },
      recover = { error -> handleFailure(error, call) },
      transform = { call.respond(it) },
    )

  suspend fun getAccount(accountId: RequestAccountId, call: ApplicationCall) =
    fold(
      block = { accountService.getAccount(AccountId(accountId.id)).toCreateAccountResponse() },
      recover = { error -> handleFailure(error, call) },
      transform = { call.respond(it) },
    )
}

private suspend fun handleFailure(error: ApplicationError, call: ApplicationCall) {
  when (error) {
    is ApplicationError.NotFoundError -> {
      call.respond(
        status = HttpStatusCode.NotFound,
        message =
          ErrorsResponse(errors = listOf(ErrorResponse(error.message)), timestamp = Instant.now()),
      )
    }

    else -> {
      call.respond(
        status = HttpStatusCode.InternalServerError,
        message =
          ErrorsResponse(errors = listOf(ErrorResponse(error.message)), timestamp = Instant.now()),
      )
    }
  }
}
