package com.fsn.template.domain.account

import java.util.UUID

interface AccountRepository {
  suspend fun getAccount(id: UUID): Account?

  suspend fun upsertAccount(account: Account): Account
}
