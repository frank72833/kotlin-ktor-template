package com.fsn.template.application

import com.fsn.template.application.account.adapter.AccountAdapter
import com.fsn.template.application.account.controller.configureAccountController
import com.fsn.template.application.account.service.AccountService
import com.fsn.template.application.configuration.configureErrorHandlers
import com.fsn.template.application.configuration.configureHealth
import com.fsn.template.application.configuration.configureSerialization
import com.fsn.template.application.configuration.configureValidation
import com.fsn.template.application.transaction.adapter.TransactionAdapter
import com.fsn.template.application.transaction.controller.configureTransactionsController
import com.fsn.template.application.transaction.service.TransactionService
import com.fsn.template.infrastructure.account.SqlAccountRepository
import com.fsn.template.infrastructure.configuration.configureDatabases
import com.fsn.template.infrastructure.configuration.configureFlyway
import com.fsn.template.infrastructure.transaction.SqlTransactionRepository
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.requestvalidation.RequestValidationException

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
  val accountRepository = SqlAccountRepository()
  val accountService = AccountService(accountRepository)
  val accountAdapter = AccountAdapter(accountService)
  configureAccountController(accountAdapter)

  // Transactions
  val repository = SqlTransactionRepository()
  val transactionService = TransactionService(
    repository = repository,
    accountService = accountService
  )
  val transactionAdapter = TransactionAdapter(transactionService)
  configureTransactionsController(transactionAdapter)
}


fun ApplicationCall.getPathParam(param: String): String =
  this.parameters[param]
    ?: throw RequestValidationException(param, listOf("Path param $param not found"))

fun Application.testModule() {
  // Testing module
}
