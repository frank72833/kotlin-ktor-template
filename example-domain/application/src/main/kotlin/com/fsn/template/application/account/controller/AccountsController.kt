package com.fsn.template.application.account.controller

import arrow.core.raise.fold
import com.fsn.template.application.account.adapter.AccountAdapter
import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.configuration.handleFailure
import com.fsn.template.application.getPathParam
import com.fsn.template.core.account.RequestAccountId
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureAccountController(accountAdapter: AccountAdapter) {
  routing {
    route("/accounts") {
      get("/{id}") {
        val accountId = RequestAccountId.fromString(call.getPathParam("id"))
        fold(
          block = { accountAdapter.getAccount(accountId) },
          recover = { error ->
            val response = handleFailure(error, call)
            call.respond(response.statusCode, response)
          },
          transform = { call.respond(it.statusCode, it) },
        )
      }

      post {
        val request = call.receive<CreateAccountApiRequest>()
        fold(
          block = { accountAdapter.createAccount(request) },
          recover = { error ->
            val response = handleFailure(error, call)
            call.respond(response.statusCode, response)
          },
          transform = { call.respond(it.statusCode, it) },
        )
      }

      put("/{id}") {
        val accountId = RequestAccountId.fromString(call.getPathParam("id"))
        val request = call.receive<UpdateAccountApiRequest>()
        fold(
          block = { accountAdapter.updateAccount(accountId, request) },
          recover = { error ->
            val response = handleFailure(error, call)
            call.respond(response.statusCode, response)
          },
          transform = { call.respond(it.statusCode, it) },
        )
      }
    }
  }
}
