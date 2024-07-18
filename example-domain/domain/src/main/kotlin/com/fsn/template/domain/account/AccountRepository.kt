package com.fsn.template.domain.account

import arrow.core.raise.Raise
import com.fsn.template.core.errors.ApplicationError

interface AccountRepository {
  context(Raise<ApplicationError>)
  suspend fun getAccount(id: AccountId): Account

  context(Raise<ApplicationError>)
  suspend fun upsertAccount(account: Account): Account
}

data class AccountNotFoundError(
  val accountId: AccountId,
  override val cause: Throwable? = null,
  override val message: String = "Account not found for id: ${accountId.value}",
) : ApplicationError.NotFoundError

data class GenericAccountRepositoryError(
  override val cause: Throwable?,
  override val message: String = "An error has occurred in AccountRepository",
) : ApplicationError.NonRetryableError
