package com.fsn.template.application

import com.fsn.template.application.account.adapter.AccountAdapter
import com.fsn.template.application.account.controller.configureAccountController
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.application.configuration.configureRouting
import com.fsn.template.application.configuration.configureSerialization
import com.fsn.template.infrastructure.account.InMemAccountRepository
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()

    // Accounts
    val accountRepository = InMemAccountRepository()
    val accountService = AccountService(accountRepository)
    val accountAdapter = AccountAdapter(accountService)
    configureAccountController(accountAdapter)

    // Transactions
}
