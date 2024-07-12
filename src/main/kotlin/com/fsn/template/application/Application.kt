package com.fsn.template.application

import com.fsn.template.application.account.adapter.AccountAdapter
import com.fsn.template.application.account.controller.configureAccountController
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.application.configuration.configureDatabases
import com.fsn.template.application.configuration.configureErrorHandlers
import com.fsn.template.application.configuration.configureFlyway
import com.fsn.template.application.configuration.configureHealth
import com.fsn.template.application.configuration.configureSerialization
import com.fsn.template.application.configuration.configureValidation
import com.fsn.template.infrastructure.account.SqlAccountRepository
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
  // API Configuration
  configureHealth()
  configureSerialization()
  configureErrorHandlers()
  configureValidation()
  val dslContext = configureDatabases()
  configureFlyway()

  // Accounts
  val accountRepository = SqlAccountRepository(dslContext)
  val accountService = AccountService(accountRepository)
  val accountAdapter = AccountAdapter(accountService)
  configureAccountController(accountAdapter)

  // Transactions
}

fun Application.testModule() {
  // Testing module
}
