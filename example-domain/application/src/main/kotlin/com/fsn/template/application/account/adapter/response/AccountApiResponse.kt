package com.fsn.template.application.account.adapter.response

import com.fsn.template.application.configuration.BigDecimalSerializer
import com.fsn.template.application.configuration.CurrencySerializer
import com.fsn.template.application.configuration.HttpStatusCodeSerializer
import com.fsn.template.application.configuration.UuidSerializer
import com.fsn.template.domain.account.Account
import io.ktor.http.HttpStatusCode
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
  @Serializable(with = HttpStatusCodeSerializer::class) val statusCode: HttpStatusCode,
  val data: AccountResponseData,
)

@Serializable
data class AccountResponseData(
  @Serializable(with = UuidSerializer::class) val id: UUID,
  val ownerName: String,
  @Serializable(with = BigDecimalSerializer::class) val balance: BigDecimal,
  @Serializable(with = CurrencySerializer::class) val currency: Currency,
)

fun Account.toCreateAccountResponse(statusCode: HttpStatusCode) =
  AccountResponse(
    statusCode = statusCode,
    data =
      AccountResponseData(
        id = id.value,
        ownerName = ownerName,
        balance = balance,
        currency = currency,
      ),
  )
