package com.fsn.template.application.account.adapter.request

import com.fsn.template.application.configuration.BigDecimalSerializer
import com.fsn.template.application.configuration.CurrencySerializer
import com.fsn.template.domain.account.Account
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

@Serializable
data class CreateAccountRequest(
    val ownerName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    @Serializable(with = CurrencySerializer::class)
    val currency: Currency
) {
    fun toDomain() = Account(
        id = UUID.randomUUID(),
        ownerName = ownerName,
        balance = BigDecimal("12"),
        currency = Currency.getInstance("EUR")
    )
}

