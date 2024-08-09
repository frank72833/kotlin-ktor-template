package com.fsn.template.application.transaction.controller

import arrow.core.raise.fold
import com.fsn.template.application.configuration.ErrorHttpResponse
import com.fsn.template.application.configuration.ErrorResponse
import com.fsn.template.application.configuration.handleFailure
import com.fsn.template.application.getPathParam
import com.fsn.template.application.transaction.adapter.TransactionAdapter
import com.fsn.template.application.transaction.adapter.request.CreateTransactionApiRequest
import com.fsn.template.core.account.RequestAccountId
import com.fsn.template.core.errors.ApplicationError
import com.fsn.template.domain.account.InvalidCurrencyAccountError
import com.fsn.template.domain.account.NotEnoughFoundsAccountError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureTransactionsController(transactionAdapter: TransactionAdapter) {
    routing {
        route("/accounts") {
            get("/{id}/transactions") {
                val accountId = RequestAccountId.fromString(call.getPathParam("id"))
                fold(
                    block = { transactionAdapter.getTransactions(accountId) },
                    recover = { error ->
                        val response = customHandleFailure(error, call)
                        call.respond(response.statusCode, response)
                    },
                    transform = { call.respond(it.statusCode, it) },
                )
            }

            post("/transactions") {
                val request = call.receive<CreateTransactionApiRequest>()
                fold(
                    block = { transactionAdapter.createTransaction(request) },
                    recover = { error ->
                        val response = customHandleFailure(error, call)
                        call.respond(response.statusCode, response)
                    },
                    transform = { call.respond(it.statusCode, it) }
                )
            }
        }
    }
}

fun customHandleFailure(error: ApplicationError, call: ApplicationCall): ErrorHttpResponse =
    when (error) {
        is NotEnoughFoundsAccountError, is InvalidCurrencyAccountError ->
            ErrorHttpResponse(
                statusCode = HttpStatusCode.BadRequest,
                errors = listOf(ErrorResponse(message = error.message, path = call.request.path())),
            )

        else -> handleFailure(error, call)
    }