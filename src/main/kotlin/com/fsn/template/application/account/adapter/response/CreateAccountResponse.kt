package com.fsn.template.application.account.adapter.response

import com.fsn.template.application.configuration.BigDecimalSerializer
import com.fsn.template.application.configuration.CurrencySerializer
import com.fsn.template.application.configuration.UuidSerializer
import com.fsn.template.domain.account.Account
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

@Serializable
data class CreateAccountResponse(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val ownerName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    @Serializable(with = CurrencySerializer::class)
    val currency: Currency
)

fun Account.toCreateAccountResponse() =
    CreateAccountResponse(
        id = id,
        ownerName = ownerName,
        balance = balance,
        currency = currency
    )

