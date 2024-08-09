package com.fsn.template.application.transaction.adapter.request

import com.fsn.template.application.configuration.BigDecimalSerializer
import com.fsn.template.core.account.RequestAccountId
import com.fsn.template.core.currencyExists
import com.fsn.template.domain.account.AccountId
import com.fsn.template.domain.transaction.command.CreateTransactionCommand
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.util.Currency

@Serializable
data class CreateTransactionApiRequest(
    val fromAccountId: String? = null,
    val toAccountId: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal? = null,
    val currencyCode: String? = null,
) {
    fun toCreateTransactionCommand() =
        CreateTransactionCommand(
            fromAccountId = AccountId.fromString(fromAccountId!!),
            toAccountId = AccountId.fromString(toAccountId!!),
            amount = amount!!,
            currency = Currency.getInstance(currencyCode!!),
        )

    fun validate(): ValidationResult {
        val reasonsList = ArrayList<String>()

        try {
            fromAccountId?.let { RequestAccountId.fromString(fromAccountId) }
            ?: reasonsList.add("From account Id cannot be empty")
        } catch(e: RequestValidationException) {
            reasonsList.add("From account Id is invalid")
        }

        try {
            toAccountId?.let { RequestAccountId.fromString(toAccountId) }
                ?: reasonsList.add("To account Id cannot be empty")
        } catch(e: RequestValidationException) {
            reasonsList.add("To account Id is invalid")
        }

        if (currencyCode.isNullOrBlank()) {
            reasonsList.add("Currency code cannot be empty")
        } else {
            if (!currencyExists(currencyCode)) reasonsList.add("Currency code does not exists")
        }

        return if (reasonsList.isEmpty()) ValidationResult.Valid
        else ValidationResult.Invalid(reasonsList)
    }
}
