package com.fsn.template.domain.account

interface AccountRepository {
  suspend fun getAccount(id: AccountId): Account?

  suspend fun upsertAccount(account: Account): Account
}
