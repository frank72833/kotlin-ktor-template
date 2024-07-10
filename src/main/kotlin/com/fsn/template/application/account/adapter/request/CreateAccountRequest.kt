package com.fsn.template.application.account.adapter.request

import com.fsn.template.core.currencyExists
import com.fsn.template.domain.account.Account
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Currency
import java.util.UUID

@Serializable
data class CreateAccountRequest(
    val ownerName: String,
    val balance: String,
    val currency: String
) {
    fun toDomain() = Account(
        id = UUID.randomUUID(),
        ownerName = ownerName,
        balance = BigDecimal("12"),
        currency = Currency.getInstance("EUR")
    )

    fun validate(): ValidationResult {
        val reasonsList = ArrayList<String>()
        if (ownerName.isBlank())
            reasonsList.add("Owner name cannot be empty")

        if (ownerName.length > 100)
            reasonsList.add("Owner name cannot longer than 100 characters")

        if (balance.toBigDecimalOrNull() == null)
            reasonsList.add("Balance has to be a number")

        if (currencyExists(currency))
            reasonsList.add("Currency does not exists")

        return if (reasonsList.isEmpty())
            ValidationResult.Valid
        else ValidationResult.Invalid(reasonsList)
    }
}