package com.fsn.template.infrastructure.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.kotlin.coroutines.transactionCoroutine

lateinit var dslContext: DSLContext

fun Application.configureDatabases(): DSLContext {
  val driverClass = environment.config.property("storage.driverClassName").getString()
  val jdbcUrl = environment.config.property("storage.jdbcUrl").getString()
  val username = environment.config.property("storage.username").getString()
  val password = environment.config.property("storage.password").getString()

  dslContext = DSL.using(
    provideDataSource(
      url = jdbcUrl,
      username = username,
      password = password,
      driverClass = driverClass,
    ),
    SQLDialect.MYSQL,
    Settings()
      .withExecuteWithOptimisticLocking(true)
      .withExecuteWithOptimisticLockingExcludeUnversioned(true)
  )

  return dslContext
}

private fun provideDataSource(
  url: String,
  username: String,
  password: String,
  driverClass: String,
): HikariDataSource =
  HikariDataSource(
    HikariConfig().apply {
      driverClassName = driverClass
      jdbcUrl = url
      setUsername(username)
      setPassword(password)
      maximumPoolSize = 3
      isAutoCommit = false
      transactionIsolation = "TRANSACTION_REPEATABLE_READ"
      validate()
    }
  )

suspend fun <A> runInTransaction(block: suspend (org.jooq.Configuration) -> A) : A =
  dslContext.transactionCoroutine { config -> block(config) }