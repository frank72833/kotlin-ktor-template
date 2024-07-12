package com.fsn.template.application.account.controller

import com.fsn.template.application.account.adapter.AccountAdapter
import com.fsn.template.application.account.adapter.request.CreateAccountApiRequest
import com.fsn.template.application.account.adapter.request.UpdateAccountApiRequest
import com.fsn.template.application.getPathParam
import io.ktor.http.HttpStatusCode
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
        val id = call.getPathParam("id")

        val response = accountAdapter.getAccount(id)
        if (response == null) {
          call.respond(HttpStatusCode.NotFound)
          return@get
        }

        call.respond(response)
      }

      post {
        val request = call.receive<CreateAccountApiRequest>()
        val response = accountAdapter.createAccount(request)
        call.respond(response)
      }

      put("/{id}") {
        val id = call.getPathParam("id")
        val request = call.receive<UpdateAccountApiRequest>()
        val response = accountAdapter.updateAccount(id, request)
        call.respond(response)
      }
    }
  }
}
