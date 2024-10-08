package com.fsn.template.infrastructure.configuration

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.configureFlyway() {
  val dbUrl = environment.config.property("storage.jdbcUrl").getString()
  val username = environment.config.property("storage.username").getString()
  val password = environment.config.property("storage.password").getString()

  val flyway =
    Flyway.configure().validateMigrationNaming(true).dataSource(dbUrl, username, password).load()

  flyway.migrate()
}
