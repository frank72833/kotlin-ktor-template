package com.fsn.template.infrastructure.configuration

import com.fsn.template.core.errors.ApplicationError
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import kotlin.reflect.KClass

fun Application.configureDatabases() {
  val driverClass = environment.config.property("storage.driverClassName").getString()
  val jdbcUrl = environment.config.property("storage.jdbcUrl").getString()
  val username = environment.config.property("storage.username").getString()
  val password = environment.config.property("storage.password").getString()

  Database.connect(
    provideDataSource(
      url = jdbcUrl,
      username = username,
      password = password,
      driverClass = driverClass,
    )
  )
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

data class OptimisticRepositoryError(
  val id: Any,
  val clazz: String,
  override val cause: Throwable? = null,
  override val message: String = "Optimistic lock repository error for entity class: $clazz and id: $id",
) : ApplicationError.RetryableError