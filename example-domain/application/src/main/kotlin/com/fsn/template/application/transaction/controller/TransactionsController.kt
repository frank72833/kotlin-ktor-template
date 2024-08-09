package com.fsn.template.application.transaction.controller

import arrow.core.raise.fold
import com.fsn.template.application.configuration.handleFailure
import com.fsn.template.application.getPathParam
import com.fsn.template.application.transaction.adapter.TransactionAdapter
import com.fsn.template.application.transaction.adapter.request.CreateTransactionApiRequest
import com.fsn.template.core.account.RequestAccountId
import io.ktor.server.application.Application
import io.ktor.server.application.call
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
                        val response = handleFailure(error, call)
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
                        val response = handleFailure(error, call)
                        call.respond(response.statusCode, response)
                    },
                    transform = { call.respond(it.statusCode, it) }
                )
            }
        }
    }
}
